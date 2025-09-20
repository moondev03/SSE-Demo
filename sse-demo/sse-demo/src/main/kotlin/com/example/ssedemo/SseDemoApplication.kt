package com.example.ssedemo

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SseDemoApplication

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        questionRoutes()
    }.start(wait = true)
}
