package com.example.repository

import com.example.data.table.ProgramTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    //connect to our database
    fun init()
    {
        Database.connect(hikari())


        //create tables
        transaction {
            SchemaUtils.create(ProgramTable)
        }
    }

    //configure our database

    fun hikari() : HikariDataSource{

        val config = HikariConfig()

        config.driverClassName = "org.postgresql.Driver"
        //config.jdbcUrl = "jdbc:postgresql:ProgramDatabase?user=postgres&password=787250"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"


        val uri = URI(System.getenv("DATABASE_URL"))
        val username = uri.userInfo.split(":").toTypedArray()[0]
        val password = uri.userInfo.split(":").toTypedArray()[1]

        config.jdbcUrl =
            "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" + "&user=$username&password=$password"
        config.validate()

        return HikariDataSource(config)

    }


    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO){
            transaction {
                block()
            }
        }
}