package web

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/number") {
                delay(3000L)
                call.respondText((Math.random() * 100).roundToInt().toString())
            }
        }
    }.start(wait = true)
}
