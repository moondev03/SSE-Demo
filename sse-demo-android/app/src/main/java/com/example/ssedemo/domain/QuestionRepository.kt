package com.example.ssedemo.domain

interface QuestionRepository {
    fun connect(onEvent: (QuestionEvent) -> Unit)

    suspend fun sendQuestion(content: String): Result<Unit>

    suspend fun voteQuestion(id: String): Result<Unit>

    suspend fun markQuestionAnswered(id: String): Result<Unit>

    suspend fun deleteQuestion(id: String): Result<Unit>
}
