package com.example.ssedemo.domain

import com.example.ssedemo.data.NonEventSourceQuestionSseClient
import com.example.ssedemo.data.QuestionClient
import com.example.ssedemo.data.QuestionSeeClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionRepositoryImpl(
    private val sseClient: QuestionSeeClient = NonEventSourceQuestionSseClient(),
    private val client: QuestionClient = QuestionClient(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : QuestionRepository {
    override fun connect(onEvent: (QuestionEvent) -> Unit) {
        CoroutineScope(dispatcher).launch {
            sseClient.connect().collect { eventResponse ->
                onEvent(eventResponse.toDomain())
            }
        }
    }

    override suspend fun sendQuestion(content: String): Result<Unit> = runCatching { client.sendQuestion(content) }

    override suspend fun voteQuestion(id: String): Result<Unit> = runCatching { client.voteQuestion(id) }

    override suspend fun markQuestionAnswered(id: String): Result<Unit> = runCatching { client.markQuestionAnswered(id) }

    override suspend fun deleteQuestion(id: String): Result<Unit> = runCatching { client.deleteQuestion(id) }
}
