package com.example.ssedemo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionRequest(
    @SerialName("content")
    val content: String,
)
