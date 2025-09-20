package com.example.ssedemo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface QuestionEvent {
    @Serializable
    @SerialName("Created")
    data class Created(
        val question: Question,
    ) : QuestionEvent

    @SerialName("Voted")
    @Serializable
    data class Voted(
        val id: String,
        val votes: Int,
    ) : QuestionEvent

    @SerialName("Deleted")
    @Serializable
    data class Deleted(
        val id: String,
    ) : QuestionEvent

    @SerialName("MarkedAnswered")
    @Serializable
    data class MarkedAnswered(
        val id: String,
    ) : QuestionEvent
}
