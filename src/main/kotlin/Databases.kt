package com.example

import UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val database = Database.connect(
        "jdbc:postgresql://localhost:5433/ktor_tutorial_db",
        "org.postgresql.Driver",
        "postgres",
        "123456"
    )

//    UserService(database)

    transaction (database) {
        SchemaUtils.create(UserTable)
    }


//    suspend fun <T> dbQuery(block: suspend () -> T): T =
//        newSuspendedTransaction(Dispatchers.IO) { block() }
}
