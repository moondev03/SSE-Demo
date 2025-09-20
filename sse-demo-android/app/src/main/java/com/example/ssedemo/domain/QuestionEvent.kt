package com.example.ssedemo.domain

import com.example.ssedemo.data.model.QuestionResponse

sealed interface QuestionEvent {
    data class Created(
        val questionResponse: QuestionResponse,
    ) : QuestionEvent

    data class Voted(
        val id: String,
        val votes: Int,
    ) : QuestionEvent

    data class Deleted(
        val id: String,
    ) : QuestionEvent

    data class MarkedAnswered(
        val id: String,
    ) : QuestionEvent
}
