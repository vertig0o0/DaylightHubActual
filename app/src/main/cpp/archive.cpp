#include <jni.h>
#include <archive.h>
#include <archive_entry.h>
#include <memory>
#include <string>
#include <array>
#include <stdexcept>

namespace {
struct ReaderDeleter { void operator()(archive* value)const{archive_read_free(value);} };
struct WriterDeleter { void operator()(archive* value)const{archive_write_free(value);} };
constexpr int64_t resource_limit=32LL*1024*1024, total_limit=2LL*1024*1024*1024;
std::string utf8(JNIEnv* env,jstring text) {
    const char* value=env->GetStringUTFChars(text,nullptr);
    if(!value)throw std::runtime_error("Could not open archive path.");
    std::string result(value);env->ReleaseStringUTFChars(text,value);return result;
}
void check(int result,archive* value) {
    if(result<ARCHIVE_OK)throw std::runtime_error(archive_error_string(value)?archive_error_string(value):"Unsupported or damaged RAR archive.");
}
void safe_name(const std::string& name) {
    if(name.empty() || name.size()>1024 || name[0]=='/' || name.find('\\')!=std::string::npos || name.find(':')!=std::string::npos)
        throw std::runtime_error("The comic contains an unsafe entry name.");
    size_t start=0;
    while(start<name.size()) { auto end=name.find('/',start);if(end==std::string::npos)end=name.size();if(name.substr(start,end-start)=="..")throw std::runtime_error("Unsafe archive path.");start=end+1; }
}
}

extern "C" JNIEXPORT jint JNICALL
Java_dev_daylight_hub_reading_NativeArchive_convert(JNIEnv* env,jobject,jstring source,jstring destination,jobject cancellation) {
    try {
        auto flag_class=env->GetObjectClass(cancellation);
        auto get=env->GetMethodID(flag_class,"get","()Z");
        auto cancelled=[&](){if(env->CallBooleanMethod(cancellation,get))throw std::runtime_error("Import cancelled.");};
        std::unique_ptr<archive,ReaderDeleter> reader(archive_read_new());
        std::unique_ptr<archive,WriterDeleter> writer(archive_write_new());
        if(!reader || !writer)throw std::runtime_error("Not enough memory to open the comic.");
        check(archive_read_support_filter_none(reader.get()),reader.get());
        check(archive_read_support_format_rar(reader.get()),reader.get());
        check(archive_read_support_format_rar5(reader.get()),reader.get());
        check(archive_read_open_filename(reader.get(),utf8(env,source).c_str(),65536),reader.get());
        check(archive_write_set_format_zip(writer.get()),writer.get());
        check(archive_write_zip_set_compression_store(writer.get()),writer.get());
        check(archive_write_open_filename(writer.get(),utf8(env,destination).c_str()),writer.get());
        archive_entry* entry=nullptr;int count=0;int64_t total=0;std::array<char,65536> buffer{};
        for(;;) {
            cancelled();auto result=archive_read_next_header(reader.get(),&entry);if(result==ARCHIVE_EOF)break;check(result,reader.get());
            if(++count>50000)throw std::runtime_error("The comic contains too many files.");
            if(archive_entry_is_encrypted(entry)>0)throw std::runtime_error("Encrypted CBR archives are not supported.");
            auto path=archive_entry_pathname_utf8(entry);if(!path)throw std::runtime_error("This archive uses an unsupported filename encoding.");
            safe_name(path);
            if(archive_entry_filetype(entry)==AE_IFDIR){check(archive_read_data_skip(reader.get()),reader.get());continue;}
            if(archive_entry_filetype(entry)!=AE_IFREG || archive_entry_symlink(entry) || archive_entry_hardlink(entry))throw std::runtime_error("Archive links are not supported.");
            auto size=archive_entry_size(entry);if(!archive_entry_size_is_set(entry) || size<0 || size>resource_limit || size>total_limit-total)throw std::runtime_error("The expanded comic exceeds its reading limits.");
            total+=size;
            std::unique_ptr<archive_entry,decltype(&archive_entry_free)> copy(archive_entry_new(),archive_entry_free);
            archive_entry_set_pathname_utf8(copy.get(),path);archive_entry_set_size(copy.get(),size);archive_entry_set_filetype(copy.get(),AE_IFREG);archive_entry_set_perm(copy.get(),0600);
            check(archive_write_header(writer.get(),copy.get()),writer.get());
            int64_t written=0;
            for(;;) {
                cancelled();auto amount=archive_read_data(reader.get(),buffer.data(),buffer.size());
                if(amount==0)break;if(amount<0)check(static_cast<int>(amount),reader.get());
                if(amount>size-written)throw std::runtime_error("An archive entry exceeds its declared size.");written+=amount;
                auto output=archive_write_data(writer.get(),buffer.data(),static_cast<size_t>(amount));if(output!=amount)throw std::runtime_error("Could not write the offline comic copy. Check free storage.");
            }
            if(written!=size)throw std::runtime_error("The archive contains an incomplete page.");
            check(archive_write_finish_entry(writer.get()),writer.get());
        }
        if(archive_read_has_encrypted_entries(reader.get())>0)throw std::runtime_error("Encrypted CBR archives are not supported.");
        check(archive_write_close(writer.get()),writer.get());check(archive_read_close(reader.get()),reader.get());
        return count;
    } catch(const std::exception& error) {
        if(!env->ExceptionCheck())env->ThrowNew(env->FindClass("java/io/IOException"),error.what());return -1;
    }
}
