package com.example

import com.example.data.db.RolesSchema
import data.db.UsersSchema
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
//    val database = Database.connect(
//        "jdbc:postgresql://localhost:5433/ktor_tutorial_db",
//        "org.postgresql.Driver",
//        "postgres",
//        "123456"
//    )

    val database = Database.connect(
        url = environment.config.property("postgres.url").getString(),
        driver = environment.config.property("postgres.driver").getString(),
        user = environment.config.property("postgres.user").getString(),
        password = environment.config.property("postgres.password").getString()
    )

//    UserService(database)

    transaction (database) {
        SchemaUtils.create(UsersSchema.UserTable)
        SchemaUtils.create(RolesSchema.Roles)
        SchemaUtils.create(RolesSchema.UserRoles)
    }


//    suspend fun <T> dbQuery(block: suspend () -> T): T =
//        newSuspendedTransaction(Dispatchers.IO) { block() }
}
