package com.example.core.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object Utils {

    suspend fun <T> suspendTransaction(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}