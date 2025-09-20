package com.example.ssedemo.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ssedemo.data.model.QuestionResponse

@Composable
fun QuestionCard(
    questionResponse: QuestionResponse,
    onUpvote: (String) -> Unit,
    onMarkAnswered: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
        colors =
            CardDefaults.cardColors().copy(
                containerColor = if (questionResponse.isAnswered) Color.LightGray else Color.White,
            ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = questionResponse.content,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = "추천: ${questionResponse.votes}")
                Row {
                    IconButton(onClick = { onUpvote(questionResponse.id) }) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "추천")
                    }
                    if (!questionResponse.isAnswered) {
                        IconButton(onClick = { onMarkAnswered(questionResponse.id) }) {
                            Icon(Icons.Default.Check, contentDescription = "완료")
                        }
                        IconButton(onClick = { onDelete(questionResponse.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun QuestionCardPreview() {
    val questionResponse =
        QuestionResponse(
            id = "1",
            content = "This is a sample question.",
            votes = 10,
            isAnswered = false,
        )
    QuestionCard(
        questionResponse = questionResponse,
        onUpvote = {},
        onMarkAnswered = {},
        onDelete = {},
    )
}
