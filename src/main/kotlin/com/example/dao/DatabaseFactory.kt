package com.example.dao

import com.example.models.Articles
import kotlinx.coroutines.Dispatchers
import org.h2.engine.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(){
        val driverClassName = "org.h2.Driver"
        val jdbcUrl = "jdbc:h2:file:./build/db"
        val database = org.jetbrains.exposed.sql.Database.connect(jdbcUrl, driverClassName)
        transaction(database) {

            SchemaUtils.create(Articles)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T) : T = newSuspendedTransaction(Dispatchers.IO) {
        block()
    }
}