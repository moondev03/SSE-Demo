package com.example.ssedemo.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ssedemo.data.model.QuestionResponse
import com.example.ssedemo.presentation.theme.SsedemoandroidTheme

@Composable
fun QuestionScreen(viewModel: QuestionViewModel = viewModel()) {
    val questions by viewModel.questions.observeAsState(emptyList())

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            QuestionInput(onSubmit = { viewModel.submitQuestion(it) })
            HorizontalDivider()
            QuestionList(
                questionResponses = questions,
                onUpvote = { viewModel.upvoteQuestion(it) },
                onMarkAnswered = { viewModel.markAnswered(it) },
                onDelete = { viewModel.deleteQuestion(it) },
            )
        }
    }
}

@Composable
private fun SlidoScreenContent(
    questionResponses: List<QuestionResponse>,
    onSubmit: (String) -> Unit,
    onUpvote: (String) -> Unit,
    onMarkAnswered: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        QuestionInput(
            onSubmit = onSubmit,
        )
        HorizontalDivider()
        QuestionList(
            questionResponses = questionResponses,
            onUpvote = onUpvote,
            onMarkAnswered = onMarkAnswered,
            onDelete = onDelete,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SlidoScreenContentPreview() {
    val questionResponses =
        listOf(
            QuestionResponse(id = "1", content = "Jetpack Compose는 무엇인가요?", votes = 10, isAnswered = false),
            QuestionResponse(id = "2", content = "Compose에서 상태(State)를 어떻게 관리하나요?", votes = 5, isAnswered = true),
            QuestionResponse(id = "3", content = "코루틴이란 무엇인가요?", votes = 12, isAnswered = false),
        )

    SsedemoandroidTheme {
        Scaffold { innerPadding ->
            SlidoScreenContent(
                questionResponses = questionResponses,
                onSubmit = { println("Submitted: $it") },
                onUpvote = { println("Upvoted: $it") },
                onMarkAnswered = { println("Marked as answered: $it") },
                onDelete = { println("Deleted: $it") },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
