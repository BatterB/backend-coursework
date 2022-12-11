package com.batterb.views.plugins

import com.batterb.domain.controllers.FileController
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

class Connection(val session: DefaultWebSocketSession, file: String) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
    val file = file
}

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val controller = FileController()

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/ws/{filename}") {
            val filename = call.parameters["filename"]!!
            val thisConnection = Connection(this,filename)
            connections += thisConnection
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    controller.writeFileData(filename,text)
                    connections.forEach{
                        if (it.file == filename){
                            it.session.send(Frame.Text(controller.getFileData(filename)))
                        }
                    }
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                        connections -= thisConnection
                    }
                }
            }
        }
    }
}
