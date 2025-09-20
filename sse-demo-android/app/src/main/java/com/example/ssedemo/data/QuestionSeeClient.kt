package com.example.ssedemo.data

import com.example.ssedemo.data.model.QuestionEventResponse
import kotlinx.coroutines.flow.Flow

interface QuestionSeeClient {
    fun connect(): Flow<QuestionEventResponse>
}
