package com.example.ssedemo.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuestionInput(
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }

    Row(modifier = modifier.padding(8.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("질문 입력") },
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onSubmit(text)
                    text = ""
                }
            },
            modifier = Modifier.padding(start = 8.dp),
        ) {
            Text("전송")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionInputPreview() {
    QuestionInput(onSubmit = {})
}
