package com.example.ssedemo.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ssedemo.data.model.QuestionResponse

@Composable
fun QuestionList(
    questionResponses: List<QuestionResponse>,
    onUpvote: (String) -> Unit,
    onMarkAnswered: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(questionResponses) { question ->
            QuestionCard(
                questionResponse = question,
                onUpvote = onUpvote,
                onMarkAnswered = onMarkAnswered,
                onDelete = onDelete,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionListPreview() {
    val questionResponses =
        listOf(
            QuestionResponse(id = "1", content = "Jetpack Compose는 무엇인가요?", votes = 10, isAnswered = false),
            QuestionResponse(id = "2", content = "Compose에서 상태(State)를 어떻게 관리하나요?", votes = 5, isAnswered = true),
            QuestionResponse(id = "3", content = "코루틴이란 무엇인가요?", votes = 12, isAnswered = false),
        )
    QuestionList(
        questionResponses = questionResponses,
        onUpvote = {},
        onMarkAnswered = {},
        onDelete = {},
    )
}
