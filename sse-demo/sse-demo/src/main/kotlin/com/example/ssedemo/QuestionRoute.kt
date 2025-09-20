package com.example.ssedemo

import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.cacheControl
import io.ktor.server.response.respond
import io.ktor.server.response.respondTextWriter
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.questionRoutes() {
    install(ContentNegotiation) { json() }

    routing {
        post("/question") {
            val body = call.receive<Map<String, String>>()
            println("\nPOST /question 요청: ${body["content"]}")
            val q = QuestionManager.createQuestion(body["content"] ?: "")
            call.respond(q)
        }

        post("/question/{id}/vote") {
            val id = call.parameters["id"]!!
            println("\nPOST /question/$id/vote 요청")
            QuestionManager.upvoteQuestion(id)
            call.respond(HttpStatusCode.OK)
        }

        post("/question/{id}/delete") {
            val id = call.parameters["id"]!!
            println("\nPOST /question/$id/delete 요청")
            QuestionManager.deleteQuestion(id)
            call.respond(HttpStatusCode.OK)
        }

        post("/question/{id}/answer") {
            val id = call.parameters["id"]!!
            println("\nPOST /question/$id/answer 요청")
            QuestionManager.markAnswered(id)
            call.respond(HttpStatusCode.OK)
        }

        get("/stream") {
            println("\nGET /stream 요청 - SSE 연결 시작")
            call.response.cacheControl(CacheControl.NoCache(null))
            val channel = QuestionManager.subscribe()

            call.respondTextWriter(contentType = ContentType.Text.EventStream) {
                try {
                    write("comment: 구독 성공\n")
                    flush()
                    println("🚀 SSE 코멘트 전송: 구독 성공")

                    channel.consumeEach { event ->
                        val jsonData = Json.encodeToString(event)
                        write("data: $jsonData\n\n")
                        flush()
                        println("🚀 SSE 데이터 전송: $jsonData")
                    }
                } finally {
                    println("SSE 종료")
                    channel.close()
                }
            }
        }
    }
}
