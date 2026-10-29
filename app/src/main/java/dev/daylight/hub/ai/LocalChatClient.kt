package dev.daylight.hub.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** OpenAI-compatible chat client restricted to loopback or private LAN IPv4 hosts. */
class LocalChatClient {
    suspend fun send(baseUrl: String, model: String, messages: JSONArray, onTool: suspend (String, JSONObject) -> String): String =
        withContext(Dispatchers.IO) {
            require(isPrivateEndpoint(baseUrl)) {
                "Use a local/private model address such as http://127.0.0.1:8080/v1 or http://192.168.1.20:8080/v1."
            }
            val endpoint = baseUrl.trimEnd('/') + "/chat/completions"
            var cycles = 0
            while (cycles++ < 4) {
                val body = JSONObject().put("model", model).put("messages", messages)
                    .put("tools", tools()).put("tool_choice", "auto")
                val response = post(endpoint, body)
                val message = response.getJSONArray("choices").getJSONObject(0).getJSONObject("message")
                val calls = message.optJSONArray("tool_calls")
                if (calls == null || calls.length() == 0) {
                    return@withContext message.optString("content", "(The model returned an empty response.)")
                }
                messages.put(message)
                for (i in 0 until calls.length()) {
                    val call = calls.getJSONObject(i)
                    val fn = call.getJSONObject("function")
                    val result = onTool(fn.getString("name"), JSONObject(fn.optString("arguments", "{}")))
                    messages.put(JSONObject().put("role", "tool").put("tool_call_id", call.getString("id")).put("content", result))
                }
            }
            "I completed the requested action."
        }

    private fun post(endpoint: String, json: JSONObject): JSONObject {
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 20_000
            readTimeout = 120_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
        }
        try {
            connection.outputStream.use { it.write(json.toString().toByteArray(Charsets.UTF_8)) }
            val stream = if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream
            val text = stream.bufferedReader().use { it.readText() }
            if (connection.responseCode !in 200..299) error("Local model error ${connection.responseCode}: $text")
            return JSONObject(text)
        } finally { connection.disconnect() }
    }

    private fun isPrivateEndpoint(value: String): Boolean {
        return try {
            val uri = java.net.URI(value)
            val host = uri.host?.removePrefix("[")?.removeSuffix("]") ?: return false
            if (uri.scheme !in listOf("http", "https")) return false
            if (host == "localhost" || host == "::1") return true
            // Accept literal private IPv4 addresses only. This avoids DNS lookups outside the device.
            val octets = host.split('.').map { it.toIntOrNull() ?: return false }
            if (octets.size != 4 || octets.any { it !in 0..255 }) return false
            val (a, b) = octets
            a == 127 || a == 10 || (a == 192 && b == 168) || (a == 172 && b in 16..31) || host == "10.0.2.2"
        } catch (_: Exception) { false }
    }

    private fun tools() = JSONArray().put(tool("create_note", "Create a note when the user asks to save or remember information.",
        JSONObject().put("title", prop("Short note title")).put("body", prop("Note contents")), listOf("title", "body")))
        .put(tool("create_planner_item", "Add a task or event to the user's planner. Use ISO-8601 date-time with timezone offset.",
            JSONObject().put("title", prop("Task or event title")).put("details", prop("Optional details"))
                .put("due_at", prop("ISO-8601 date and time, for example 2026-10-30T09:00:00+03:00")), listOf("title", "due_at")))
        .put(tool("list_notes", "Find the user's existing notes when asked.", JSONObject(), emptyList()))
        .put(tool("list_planner", "Find the user's planner items when asked.", JSONObject(), emptyList()))

    private fun tool(name: String, description: String, properties: JSONObject, required: List<String>) = JSONObject()
        .put("type", "function").put("function", JSONObject().put("name", name).put("description", description)
            .put("parameters", JSONObject().put("type", "object").put("properties", properties).put("required", JSONArray(required))))
    private fun prop(description: String) = JSONObject().put("type", "string").put("description", description)
}
