package com.example.ssedemo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    @SerialName("id")
    val id: String,
    @SerialName("content")
    val content: String,
    @SerialName("votes")
    var votes: Int = 0,
    @SerialName("isAnswered")
    var isAnswered: Boolean = false,
)
