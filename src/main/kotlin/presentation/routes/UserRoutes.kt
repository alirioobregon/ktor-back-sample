package com.example.presentation.routes

import com.example.data.repository.UserRepositoryImpl
import com.example.domain.usecase.UserUseCase
import com.example.models.Task
import data.db.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.SerializationException

fun Route.userRoutes() {

    val repository = UserRepositoryImpl()
    val useCase = UserUseCase(repository)

    route("/users") {
        get {
            val users = useCase.allUsers()
            call.respond(users)
        }

        post {
            try {
                val user = call.receive<User>()
                useCase.addNewUser(user)
                call.respond(HttpStatusCode.Created)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}


