package com.batterb

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.batterb.views.plugins.configureRouting
import com.batterb.views.plugins.configureSecurity
import com.batterb.views.plugins.configureSerialization
import com.batterb.views.plugins.configureSockets
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/sharetask",driver = "org.postgresql.Driver",
        user = "postgres", password = "admin")
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureSockets()
    configureRouting()
}
