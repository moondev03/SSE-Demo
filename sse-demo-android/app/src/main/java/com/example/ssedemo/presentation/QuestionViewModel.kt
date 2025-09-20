package com.example.ssedemo.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ssedemo.data.model.QuestionResponse
import com.example.ssedemo.domain.QuestionEvent
import com.example.ssedemo.domain.QuestionRepository
import com.example.ssedemo.domain.QuestionRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepository: QuestionRepository = QuestionRepositoryImpl(),
) : ViewModel() {
    private val _questions = MutableLiveData<List<QuestionResponse>>(emptyList())
    val questions: LiveData<List<QuestionResponse>> get() = _questions

    private val currentQuestionResponses: List<QuestionResponse> get() = _questions.value.orEmpty()

    init {
        questionRepository.connect { event ->
            viewModelScope.launch {
                when (event) {
                    is QuestionEvent.Created,
                    -> _questions.value = currentQuestionResponses + event.questionResponse

                    is QuestionEvent.Voted -> {
                        _questions.value =
                            currentQuestionResponses.map { question ->
                                if (question.id != event.id) return@map question
                                question.copy(votes = event.votes)
                            }
                    }

                    is QuestionEvent.Deleted ->
                        _questions.value = currentQuestionResponses.filter { it.id != event.id }

                    is QuestionEvent.MarkedAnswered ->
                        _questions.value =
                            currentQuestionResponses.map { question ->
                                if (question.id != event.id) return@map question
                                question.copy(isAnswered = true)
                            }
                }
            }
        }
    }

    fun submitQuestion(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            questionRepository
                .sendQuestion(content)
                .onSuccess { Log.d("QuestionViewModel", "질문 생성 성공") }
                .onFailure { Log.e("QuestionViewModel", "질문 생성 실패", it) }
        }
    }

    fun upvoteQuestion(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            questionRepository
                .voteQuestion(id)
                .onSuccess { Log.d("QuestionViewModel", "추천 성공") }
                .onFailure { Log.e("QuestionViewModel", "추천 실패", it) }
        }
    }

    fun markAnswered(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            questionRepository
                .markQuestionAnswered(id)
                .onSuccess { Log.d("QuestionViewModel", "완료 처리 성공") }
                .onFailure { Log.e("QuestionViewModel", "완료 처리 실패", it) }
        }
    }

    fun deleteQuestion(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            questionRepository
                .deleteQuestion(id)
                .onSuccess { Log.d("QuestionViewModel", "삭제 성공") }
                .onFailure { Log.e("QuestionViewModel", "삭제 실패", it) }
        }
    }
}
