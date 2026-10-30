#include <jni.h>
#include <llama.h>
#include <atomic>
#include <string>
#include <vector>
#include <stdexcept>
#include <algorithm>
#include <memory>

static llama_model * model = nullptr;
static llama_context * ctx = nullptr;
static std::atomic_bool cancelled{false};
struct LoadReporter {
    JNIEnv * env;
    jobject listener;
    jmethodID callback;
    int last_percent = -1;
    bool report(const char * stage, int percent = -1) {
        if (cancelled || env->ExceptionCheck()) return false;
        auto text = env->NewStringUTF(stage);
        if (!text) return false;
        env->CallVoidMethod(listener, callback, text, percent);
        env->DeleteLocalRef(text);
        return !cancelled && !env->ExceptionCheck();
    }
};
static std::string str(JNIEnv * env, jstring value) {
    if (!value) return "";
    auto cls = env->FindClass("java/lang/String");
    auto getBytes = env->GetMethodID(cls, "getBytes", "(Ljava/lang/String;)[B");
    auto encoding = env->NewStringUTF("UTF-8");
    auto bytes = (jbyteArray) env->CallObjectMethod(value, getBytes, encoding);
    std::string out(env->GetArrayLength(bytes), '\0');
    env->GetByteArrayRegion(bytes, 0, out.size(), reinterpret_cast<jbyte *>(out.data()));
    env->DeleteLocalRef(bytes); env->DeleteLocalRef(encoding); env->DeleteLocalRef(cls); return out;
}
static void fail(JNIEnv * env, const std::string & text) {
    env->ThrowNew(env->FindClass("java/lang/IllegalStateException"), text.c_str());
}
static void release_model() {
    if (ctx) { llama_free(ctx); ctx = nullptr; }
    if (model) { llama_model_free(model); model = nullptr; }
}
extern "C" JNIEXPORT void JNICALL
Java_dev_daylight_hub_ai_NativeBridge_cancel(JNIEnv *, jobject) { cancelled = true; }
extern "C" JNIEXPORT void JNICALL
Java_dev_daylight_hub_ai_NativeBridge_prepare(JNIEnv *, jobject) { cancelled = false; }
extern "C" JNIEXPORT void JNICALL
Java_dev_daylight_hub_ai_NativeBridge_unload(JNIEnv *, jobject) { release_model(); }
extern "C" JNIEXPORT jstring JNICALL
Java_dev_daylight_hub_ai_NativeBridge_load(JNIEnv * env, jobject, jstring path, jint context, jint threads, jobject listener) {
    try {
        release_model();
        auto cls = env->GetObjectClass(listener);
        LoadReporter reporter{env, listener, env->GetMethodID(cls, "onProgress", "(Ljava/lang/String;I)V")};
        env->DeleteLocalRef(cls);
        if (!reporter.report("Reading GGUF metadata")) throw std::runtime_error("Model loading cancelled.");
        llama_backend_init();
        auto mp = llama_model_default_params(); mp.n_gpu_layers = 0;
        // Keep weights file-backed; avoid an additional repacked copy on RAM-limited phones.
        mp.load_mode = LLAMA_LOAD_MODE_MMAP;
        mp.use_extra_bufts = false;
        mp.progress_callback = [](float value, void * data) {
            auto * progress = static_cast<LoadReporter *>(data);
            const int percent = std::clamp(static_cast<int>(value * 100), 0, 100);
            if (percent == progress->last_percent) return !cancelled.load();
            progress->last_percent = percent;
            return progress->report("Mapping model weights", percent);
        };
        mp.progress_callback_user_data = &reporter;
        model = llama_model_load_from_file(str(env, path).c_str(), mp);
        if (cancelled) throw std::runtime_error("Model loading cancelled.");
        if (!model) throw std::runtime_error("Unable to load this GGUF. Use a supported text model that fits available RAM.");
        if (llama_model_has_encoder(model)) throw std::runtime_error("Import a decoder text chat model.");
        const char * tmpl = llama_model_chat_template(model, nullptr);
        llama_chat_message test[] = {{"user", "Hello"}};
        if (!tmpl || llama_chat_apply_template(tmpl, test, 1, true, nullptr, 0) < 0)
            throw std::runtime_error("This GGUF has no supported chat template. Import a supported instruct/chat model.");
        if (!reporter.report("Allocating context and preparing CPU")) throw std::runtime_error("Model loading cancelled.");
        auto cp = llama_context_default_params();
        cp.n_ctx = std::min(context, llama_model_n_ctx_train(model));
        cp.n_batch = 256; cp.n_ubatch = 128; cp.n_threads = threads; cp.n_threads_batch = threads;
        cp.abort_callback = [](void *) { return cancelled.load(); };
        ctx = llama_init_from_model(model, cp);
        if (cancelled) throw std::runtime_error("Model loading cancelled.");
        if (!ctx) throw std::runtime_error("Could not allocate context. Lower context size or use a smaller GGUF.");
        char desc[512]; llama_model_desc(model, desc, sizeof(desc));
        return env->NewStringUTF(desc);
    } catch (const std::exception & e) { release_model(); if (!env->ExceptionCheck()) fail(env, e.what()); return nullptr; }
}
extern "C" JNIEXPORT void JNICALL
Java_dev_daylight_hub_ai_NativeBridge_generate(JNIEnv * env, jobject, jobjectArray roles, jobjectArray contents,
    jfloat temperature, jfloat topP, jint topK, jfloat minP, jfloat repeat, jlong seed,
    jint maxTokens, jstring grammar, jobject listener) {
    try {
        if (!model || !ctx) throw std::runtime_error("Load a model in Settings first.");
        const int count = env->GetArrayLength(roles);
        std::vector<std::string> rs, cs;
        for (int i = 0; i < count; ++i) {
            auto r = (jstring) env->GetObjectArrayElement(roles, i);
            auto c = (jstring) env->GetObjectArrayElement(contents, i);
            rs.push_back(str(env, r)); cs.push_back(str(env, c));
            env->DeleteLocalRef(r); env->DeleteLocalRef(c);
        }
        auto vocab = llama_model_get_vocab(model);
        std::vector<llama_token> tokens;
        std::string prompt;
        while (true) {
            std::vector<llama_chat_message> messages;
            for (size_t i=0; i<rs.size(); ++i) messages.push_back({rs[i].c_str(), cs[i].c_str()});
            auto tmpl = llama_model_chat_template(model, nullptr);
            int n = llama_chat_apply_template(tmpl, messages.data(), messages.size(), true, nullptr, 0);
            if (n < 0) throw std::runtime_error("Unsupported model chat template.");
            std::vector<char> formatted(n + 1);
            llama_chat_apply_template(tmpl, messages.data(), messages.size(), true, formatted.data(), formatted.size());
            prompt.assign(formatted.data(), n);
            int needed = -llama_tokenize(vocab, prompt.data(), prompt.size(), nullptr, 0, true, true);
            if (needed <= 0) throw std::runtime_error("Tokenization failed.");
            tokens.resize(needed);
            int actual = llama_tokenize(vocab, prompt.data(), prompt.size(), tokens.data(), tokens.size(), true, true);
            if (actual < 0) throw std::runtime_error("Tokenization failed.");
            tokens.resize(actual);
            if (actual + maxTokens < (int)llama_n_ctx(ctx)) break;
            if (rs.size() <= 2) throw std::runtime_error("Request exceeds context. Shorten it, lower reply length, or increase context.");
            size_t remove = rs.size() > 3 ? 2 : 1;
            rs.erase(rs.begin()+1, rs.begin()+1+remove);
            cs.erase(cs.begin()+1, cs.begin()+1+remove);
        }
        llama_memory_clear(llama_get_memory(ctx), true);
        for (size_t i = 0; i < tokens.size(); i += 256) {
            if (cancelled) return;
            int n = std::min<size_t>(256, tokens.size()-i);
            if (llama_decode(ctx, llama_batch_get_one(tokens.data()+i, n))) {
                if (cancelled) return; throw std::runtime_error("Prompt evaluation failed; reduce context size.");
            }
        }
        auto chain = std::unique_ptr<llama_sampler, decltype(&llama_sampler_free)>(
            llama_sampler_chain_init(llama_sampler_chain_default_params()), llama_sampler_free);
        std::string g = str(env, grammar);
        if (!g.empty()) {
            auto smpl = llama_sampler_init_grammar(vocab, g.c_str(), "root");
            if (!smpl) throw std::runtime_error("Action grammar could not be initialized.");
            llama_sampler_chain_add(chain.get(), smpl);
        }
        llama_sampler_chain_add(chain.get(), llama_sampler_init_penalties(llama_vocab_n_tokens(vocab), 64, repeat, 0, 0));
        llama_sampler_chain_add(chain.get(), llama_sampler_init_top_k(topK));
        llama_sampler_chain_add(chain.get(), llama_sampler_init_top_p(topP, 1));
        llama_sampler_chain_add(chain.get(), llama_sampler_init_min_p(minP, 1));
        llama_sampler_chain_add(chain.get(), llama_sampler_init_temp(temperature));
        llama_sampler_chain_add(chain.get(), temperature == 0 ? llama_sampler_init_greedy() :
            llama_sampler_init_dist(seed < 0 ? LLAMA_DEFAULT_SEED : (uint32_t)seed));
        auto cls = env->GetObjectClass(listener);
        auto callback = env->GetMethodID(cls, "onPiece", "([B)V");
        for (int i=0; i<maxTokens && !cancelled; ++i) {
            auto token = llama_sampler_sample(chain.get(), ctx, -1);
            if (llama_vocab_is_eog(vocab, token)) break;
            std::vector<char> piece(256);
            int n = llama_token_to_piece(vocab, token, piece.data(), piece.size(), 0, false);
            if (n < 0) { piece.resize(-n); n = llama_token_to_piece(vocab, token, piece.data(), piece.size(), 0, false); }
            if (n > 0) {
                auto bytes = env->NewByteArray(n);
                env->SetByteArrayRegion(bytes, 0, n, reinterpret_cast<jbyte *>(piece.data()));
                env->CallVoidMethod(listener, callback, bytes); env->DeleteLocalRef(bytes);
                if (env->ExceptionCheck()) return;
            }
            if (llama_decode(ctx, llama_batch_get_one(&token, 1))) {
                if (cancelled) return; throw std::runtime_error("Generation failed; reduce context size.");
            }
        }
    } catch (const std::exception & e) { fail(env, e.what()); }
}
