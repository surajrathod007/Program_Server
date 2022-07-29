package com.example.Routes

import com.example.data.model.Program
import com.example.data.model.SimpleResponse
import com.example.data.model.Update
import com.example.repository.repo
import com.example.semReqCount
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val API_VERSION = "/v1"
const val PROGRAMS = "$API_VERSION/programs"
const val ADD_PROGRAM = "$PROGRAMS/add"
const val SPECIFIC = "$PROGRAMS/specific"
const val UPDATE = "$API_VERSION/change"
const val ADD = "$API_VERSION/update"
const val GETUPDATE = "$API_VERSION/get"


@Location(ADD_PROGRAM)
class AddProgramRoute

@Location(PROGRAMS)
class GetPrograms

@Location(SPECIFIC)
class Specific

@Location(UPDATE)
class UpdateApp

@Location(ADD)
class AddUpdate

@Location(GETUPDATE)
class GetUpdate


fun Route.ProgramRoute(
    db : repo
){


    post<UpdateApp>{

        val updateRequest = try{
            call.receive<Update>()
        }catch (e : Exception)
        {
            call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"missing fields"))
            return@post
        }

        //add update
        try{

            val update = Update(updateRequest.id,updateRequest.version,updateRequest.link,updateRequest.message)

            db.changeUpdate(update)
            call.respond(HttpStatusCode.OK,SimpleResponse(true,"Update added!"))

        }catch (e : Exception)
        {
            call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some problem ocuured"))
        }



    }

    get<GetUpdate>{

        val update = db.getUpdate()
        call.respond(
            HttpStatusCode.OK,update!!
        )
    }





    post<AddUpdate>{

        //get update from body
        val updateRequest = try{
            call.receive<Update>()
        }catch (e : Exception)
        {
            call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"missing fields"))
            return@post
        }


        //add update
        try{

            val update = Update(updateRequest.id,updateRequest.version,updateRequest.link,updateRequest.message)

            db.addUpdate(update)
            call.respond(HttpStatusCode.OK,SimpleResponse(true,"Update added!"))

        }catch (e : Exception)
        {
            call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some problem ocuured"))
        }


    }



    get<GetPrograms>{

        val program = db.getAllProgram()
        call.respond(
            HttpStatusCode.OK,program
        )
    }

    get<Specific>{

        val sem = call.request.queryParameters["sem"]
        val sub = call.request.queryParameters["sub"]
        val unit = call.request.queryParameters["unit"]

        //var s = sem?.get(3)?.toInt()



        // Semester Query Request Counter
       // semReqCount[(sem!!.toInt() -1)]+=1

        when(sem){
            "Sem 1" -> {
                db.plus(1)
            }
            "Sem 2" -> {
                db.plus(2)
            }
            "Sem 3" -> {
                db.plus(3)
            }
            "Sem 4" -> {
                db.plus(4)
            }
            "Sem 5" -> {
                db.plus(5)
            }
            "Sem 6" -> {
                db.plus(6)
            }

        }


        val program = db.getSpecificProgram(sem!!,sub!!,unit!!)

        if(program.isEmpty())
        {
            call.respond(HttpStatusCode.OK,"No Programs")
            return@get
        }

        call.respond(HttpStatusCode.OK,program)
    }

    post<AddProgramRoute>{


        //get program from body
        val programRequest = try{
            call.receive<Program>()
        }catch (e : Exception)
        {
            call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"missing fields"))
            return@post
        }


        //add program

        try{

            val program = Program(programRequest.id,
            programRequest.title,
            programRequest.content,programRequest.sem,
            programRequest.sub,
            programRequest.unit)

            db.addProgram(program)
            call.respond(HttpStatusCode.OK,SimpleResponse(true,"Program added!"))

        }catch (e : Exception)
        {
            call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some problem ocuured"))
        }


        //add below body

        /*
        {
    "id" : "1",
     "title" : "How to add two numbers",
     "content" : "Just use + operator",
     "sem" : "sem 1",
     "sub" : "dbms",
     "unit" : "Unit - 4"
        }
         */


    }



}