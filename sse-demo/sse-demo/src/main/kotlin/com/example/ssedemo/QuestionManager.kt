package com.example.ssedemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

object QuestionManager {
    private val questions = mutableMapOf<String, Question>()
    private val subscribers = CopyOnWriteArrayList<SendChannel<QuestionEvent>>()

    fun createQuestion(content: String): Question {
        val q =
            Question(
                id = UUID.randomUUID().toString(),
                content = content,
            )
        questions[q.id] = q
        println("✅ 질문 생성: ${q.id} -> ${q.content}")
        broadcast(QuestionEvent.Created(q))
        return q
    }

    fun upvoteQuestion(id: String) {
        val q = questions[id] ?: run {
            println("⚠️ 추천 실패: $id 없음")
            return
        }
        q.votes++
        println("👍 추천: ${q.id}, 현재 추천 수: ${q.votes}")
        broadcast(QuestionEvent.Voted(q.id, q.votes))
    }

    fun deleteQuestion(id: String) {
        if (questions.remove(id) != null) {
            println("🗑️ 삭제: $id")
            broadcast(QuestionEvent.Deleted(id))
        } else {
            println("⚠️ 삭제 실패: $id 없음")
        }
    }

    fun markAnswered(id: String) {
        questions[id]?.let {
            it.isAnswered = true
            println("✅ 완료 처리: $id")
            broadcast(QuestionEvent.MarkedAnswered(it.id))
        } ?: println("⚠️ 완료 처리 실패: $id 없음")
    }

    fun subscribe(): Channel<QuestionEvent> {
        val channel = Channel<QuestionEvent>(Channel.UNLIMITED)
        subscribers += channel

        questions.values.forEach {
            channel.trySend(QuestionEvent.Created(it))
        }
        println("📡 새로운 SSE 구독자 추가, 총 구독자 수: ${subscribers.size}")
        return channel
    }

    private fun broadcast(event: QuestionEvent) {
        println("📢 이벤트 브로드캐스트: $event")
        subscribers.forEach { ch ->
            ch.trySendBlocking(event).onFailure {
                println("⚠️ 구독자 전송 실패, 제거: $it")
                unsubscribe(ch)
            }
        }
    }

    private fun unsubscribe(channel: SendChannel<QuestionEvent>) {
        subscribers.remove(channel)
        println("️️⚠️ 구독자 연결 끊김, 제거: 총 구독자 수: ${subscribers.size}")
    }
}
