package com.example.ssedemo.data

import com.example.ssedemo.data.model.QuestionRequest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class QuestionClient(
    private val client: OkHttpClient = OkHttpClient(),
) {
    private val jsonMediaType = "application/json".toMediaType()

    fun sendQuestion(content: String) {
        val body = Json.encodeToString(QuestionRequest(content)).toRequestBody(jsonMediaType)
        val request = createRequest("/question", body)
        client.newCall(request).execute().use { response ->
            require(response.isSuccessful) { "질문 생성 실패: ${response.code}" }
        }
    }

    fun voteQuestion(id: String) {
        val request = createRequest("/question/$id/vote", "".toRequestBody())
        client.newCall(request).execute().use { response ->
            require(response.isSuccessful) { "추천 실패: ${response.code}" }
        }
    }

    fun markQuestionAnswered(id: String) {
        val request = createRequest("/question/$id/answer", "".toRequestBody())
        client.newCall(request).execute().use { response ->
            require(response.isSuccessful) { "완료 처리 실패: ${response.code}" }
        }
    }

    fun deleteQuestion(id: String) {
        val request = createRequest("/question/$id/delete", "".toRequestBody())
        client.newCall(request).execute().use { response ->
            require(response.isSuccessful) { "삭제 실패: ${response.code}" }
        }
    }

    private fun createRequest(
        path: String,
        body: RequestBody,
    ): Request =
        Request
            .Builder()
            .url("http://10.0.2.2:8080$path")
            .post(body)
            .build()
}
