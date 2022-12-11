package com.batterb.views.plugins

import com.batterb.domain.controllers.FileController
import com.batterb.views.viewobjects.FileVO
import com.batterb.views.viewobjects.UserVO
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.io.File

fun Application.configureRouting() {

    val controller = FileController()
    routing {
        post("/file") {
            val requestBody = call.receive<FileVO>()
            val file = File("./src/main/resources/files/${requestBody.fileName}.txt")
            call.respondText(file.readText())
        }
        post("/fileCreation") {
            val requestBody = call.receive<UserVO>()
            controller.createFile(requestBody.currentEditingFile!!.fileName,requestBody.username)
            call.respondText("Success")

        }

        post("/fileGetData") {
            val requestBody = call.receive<UserVO>()
            val file = controller.getServerFilename(requestBody)
            call.respondText(controller.getFileData(file))
        }

        get("/getAllFiles/{userId}") {
            val user = UserVO(
                username = call.parameters["userId"]!!,
                currentEditingFile = null
            )
            call.respond(controller.getAllUserFiles(user))
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
