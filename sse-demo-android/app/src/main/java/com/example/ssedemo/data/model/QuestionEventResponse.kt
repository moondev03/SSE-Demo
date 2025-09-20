package com.example.ssedemo.data.model

import com.example.ssedemo.domain.QuestionEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface QuestionEventResponse {
    @Serializable
    @SerialName("Created")
    data class Created(
        val question: QuestionResponse,
    ) : QuestionEventResponse

    @Serializable
    @SerialName("Voted")
    data class Voted(
        val id: String,
        val votes: Int,
    ) : QuestionEventResponse

    @Serializable
    @SerialName("Deleted")
    data class Deleted(
        val id: String,
    ) : QuestionEventResponse

    @Serializable
    @SerialName("MarkedAnswered")
    data class MarkedAnswered(
        val id: String,
    ) : QuestionEventResponse

    fun toDomain(): QuestionEvent =
        when (this) {
            is Created -> QuestionEvent.Created(question)
            is Voted -> QuestionEvent.Voted(id, votes)
            is Deleted -> QuestionEvent.Deleted(id)
            is MarkedAnswered -> QuestionEvent.MarkedAnswered(id)
        }
}
