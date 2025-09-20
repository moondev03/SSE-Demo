package com.example.ssedemo

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val content: String,
    var votes: Int = 0,
    var isAnswered: Boolean = false,
)
