package com.example

import com.example.Routes.ProgramRoute
import com.example.data.table.VisitCountTable
import com.example.repository.DatabaseFactory
import com.example.repository.repo
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.gson.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.selectAll

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    //initialse databse

    DatabaseFactory.init()

    var totalReq = 0

    val db = repo()

    install(Locations) {
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {


        ProgramRoute(db)




        get("/") {
            call.respondText("HELLO WORLD! Cool ", contentType = ContentType.Text.Plain)
        }
        get("/stats"){
            /*for (i in semReqCount.indices){
                call.respondText("Sem ${i+1} : ${semReqCount[i]}\n")
            }*/
            // May be expensive since 6 times traversing through the table
            for(i in 1 until 7){
                call.respondText("${db.getCount(i)}")
            }
            // Should print complete table
           call.respondText( "${VisitCountTable.selectAll()}")

        }
        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }

        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}") data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}

data class MySession(val count: Int = 0)

