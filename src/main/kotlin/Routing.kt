package com.example

import com.example.data.repository.PostgresTaskRepository
import com.example.presentation.routes.userRoutes
import domain.repository.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import presentation.routes.taskRoutes
import java.lang.Exception

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }


    routing {

        staticResources("static", "static")

//        val taskRepositoryImpl: domain.repository.TaskRepository = TaskRepositoryImpl()


        get("/") {
            call.respondText("Hello World!")
        }

        get("/test") {
            call.respond(mapOf("message" to "Hola soy test"))
        }

        get("/error") {
            throw IllegalStateException("Errorrrrr")
        }

        get("/param/{param?}") {
            try {
                val param = call.parameters["param"]
                if (param == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                call.respond("Hola $param")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        taskRoutes()
        userRoutes()

    }
}
