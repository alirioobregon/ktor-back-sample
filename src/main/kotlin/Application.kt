package com.example

import com.example.data.repository.PostgresTaskRepository
import com.example.data.repository.TaskRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.swagger.SwaggerConfig

fun main(args: Array<String>) {
//    embeddedServer(
//        Netty,
//        port = 9292,
//        host = "0.0.0.0",
//        module = Application::module,
//    ).start(wait = true)
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

//    val repository = TaskRepositoryImpl()
    configureFrameworks()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting()
}
