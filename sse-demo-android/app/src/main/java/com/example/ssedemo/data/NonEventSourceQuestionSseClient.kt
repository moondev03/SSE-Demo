package com.example.ssedemo.data

import android.util.Log
import com.example.ssedemo.data.model.QuestionEventResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

class NonEventSourceQuestionSseClient(
    private val client: OkHttpClient =
        OkHttpClient
            .Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .build(),
) : QuestionSeeClient {
    private val json: Json =
        Json {
            classDiscriminator = "type"
            ignoreUnknownKeys = true
        }

    override fun connect(): Flow<QuestionEventResponse> =
        callbackFlow {
            val call = client.newCall(buildRequest())
            call.enqueue(buildCallback(::trySend))
            awaitClose { call.cancel() }
        }

    private fun buildRequest(): Request =
        Request
            .Builder()
            .url("http://10.0.2.2:8080/stream")
            .build()

    private fun buildCallback(emit: (QuestionEventResponse) -> Unit): Callback =
        object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException,
            ) {
                Log.e("SSE", "연결 실패: ${e.message}", e)
            }

            override fun onResponse(
                call: Call,
                response: Response,
            ) {
                response.body.use { body ->
                    val source = body.source()
                    while (true) {
                        val line = source.readUtf8Line() ?: break
                        Log.d("SSE", "Received line: $line")
                        handleIncomingLine(line, emit)
                    }
                }
            }
        }

    private fun handleIncomingLine(
        line: String,
        emit: (QuestionEventResponse) -> Unit,
    ) {
        if (!line.startsWith("data:")) return
        val jsonString = line.removePrefix("data:").trim()
        parseEventOrNull(jsonString)?.let(emit)
    }

    private fun parseEventOrNull(jsonString: String): QuestionEventResponse? =
        runCatching {
            json.decodeFromString<QuestionEventResponse>(jsonString)
        }.onFailure { exception ->
            Log.e("SSE", "JSON 파싱 실패: ${exception.message}", exception)
        }.getOrNull()
}
