package dev.daylight.hub.ai

import org.json.JSONObject

data class GenerationParameters(
    val temperature: Float = .7f, val topP: Float = .9f, val topK: Int = 40,
    val minP: Float = .05f, val repetitionPenalty: Float = 1.1f, val seed: Long = -1,
    val maxTokens: Int = 512, val contextSize: Int = 2048,
    val threads: Int = minOf(2, (Runtime.getRuntime().availableProcessors()-1).coerceAtLeast(1)),
    val systemPrompt: String = "You are a helpful, private offline assistant."
) {
    fun validate() {
        require(temperature.isFinite() && temperature in 0f..2f) { "Temperature must be 0–2." }
        require(topP.isFinite() && topP > 0 && topP <= 1 && minP.isFinite() && minP in 0f..1f) { "Top-p must be >0–1; min-p must be 0–1." }
        require(topK in 0..1000 && repetitionPenalty.isFinite() && repetitionPenalty in .1f..3f) { "Invalid top-k or repetition penalty." }
        require(contextSize in 512..32768 && maxTokens in 32 until contextSize - 128) { "Reply length must fit inside context with room for the prompt." }
        require(threads in 1..Runtime.getRuntime().availableProcessors().coerceAtLeast(1)) { "Thread count exceeds available CPUs." }
        require(seed in -1..4294967295L) { "Use -1 for random, or an unsigned 32-bit seed." }
        require(systemPrompt.length <= 16000) { "System prompt is too long." }
    }
    fun json() = JSONObject().put("temperature", temperature).put("topP", topP).put("topK", topK)
        .put("minP", minP).put("repetitionPenalty", repetitionPenalty).put("seed", seed).put("maxTokens", maxTokens)
        .put("contextSize", contextSize).put("threads", threads).put("systemPrompt", systemPrompt).toString()
    fun describe()="Temperature: $temperature · Top-p: $topP · Top-k: $topK\nMin-p: $minP · Repetition penalty: $repetitionPenalty\nSeed: "+(if(seed<0) "Random" else seed.toString())+"\nReply limit: $maxTokens tokens · Context: $contextSize tokens\nCPU threads: $threads\nSystem prompt:\n$systemPrompt"
    companion object {
        fun parse(text: String): GenerationParameters {
            val j = JSONObject(text); val d = GenerationParameters()
            return GenerationParameters(j.optDouble("temperature", .7).toFloat(), j.optDouble("topP", .9).toFloat(),
                j.optInt("topK", 40), j.optDouble("minP", .05).toFloat(), j.optDouble("repetitionPenalty", 1.1).toFloat(),
                j.optLong("seed", -1), j.optInt("maxTokens", 512), j.optInt("contextSize", 2048),
                j.optInt("threads", d.threads), j.optString("systemPrompt", d.systemPrompt)).also { it.validate() }
        }
    }
}
