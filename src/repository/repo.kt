package com.example.repository

import com.example.data.model.Program
import com.example.data.model.Update
import com.example.data.model.VisitCount
import com.example.data.table.ProgramTable
import com.example.data.table.UpdateTable
import com.example.data.table.VisitCountTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class repo {



    //add update

    suspend fun addUpdate(update : Update){

        dbQuery {
            UpdateTable.insert { uTable->

                uTable[UpdateTable.id] = update.id
                uTable[UpdateTable.version] = update.version
                uTable[UpdateTable.link] = update.link
                uTable[UpdateTable.message] = update.message

            }
        }
    }

    suspend fun changeUpdate(update : Update){
        dbQuery {
            UpdateTable.update{ uTable->

                uTable[UpdateTable.version] = update.version
                uTable[UpdateTable.link] = update.link
                uTable[UpdateTable.message] = update.message

            }
        }
    }

    suspend fun addProgram(program : Program){
        dbQuery{
            ProgramTable.insert { pTable->

                pTable[ProgramTable.id] = program.id
                pTable[ProgramTable.title] = program.title
                pTable[ProgramTable.content] = program.content
                pTable[ProgramTable.sem] = program.sem
                pTable[ProgramTable.sub] = program.sub
                pTable[ProgramTable.unit] = program.unit

            }
        }

    }

    suspend fun plus(id : Int){
        var c = getCount(id)
        var count = 0
        if(c != null) {

            count = c.count
        }
        count++
        dbQuery {
            VisitCountTable.update({VisitCountTable.id eq id}){table ->
                table[VisitCountTable.count] = count
            }
        }
    }

    suspend fun getCount(id : Int) : VisitCount? = dbQuery {
        VisitCountTable.select {
            VisitCountTable.id.eq(id)
        }.map{
            rowToVisitCount(it)
        }.singleOrNull()
    }



    suspend fun getSpecificProgram( sem : String,sub : String,unit : String):List<Program?> = dbQuery {

        ProgramTable.select {
            ProgramTable.sem.eq(sem) and ProgramTable.sub.eq(sub) and ProgramTable.unit.eq(unit)
        }.map {
            rowToProgram(it)
        }
    }


    suspend fun getAllProgram() : List<Program?> = dbQuery {
        ProgramTable.selectAll().map{
            rowToProgram(it)
        }
    }

    suspend fun getUpdate() : Update? = dbQuery {
        UpdateTable.select {
            UpdateTable.id.eq(1)
        }.map { rowToUpdate(it) }.singleOrNull()
    }




    //util fucntion to convert a table row to an object
    private fun rowToProgram(row : ResultRow?):Program?{
        if(row==null)
        {
            return null
        }

        return Program(
            id = row[ProgramTable.id],
            title = row[ProgramTable.title],
            content = row[ProgramTable.content],
            sem = row[ProgramTable.sem],
            sub = row[ProgramTable.sub],
            unit = row[ProgramTable.unit]
        )


    }

    private fun rowToUpdate(row : ResultRow?):Update?{
        if(row==null)
        {
            return null
        }

        return Update(
            id = row[UpdateTable.id],
            version = row[UpdateTable.version],
            link = row[UpdateTable.link],
            message = row[UpdateTable.message],

        )


    }

    //row to VisitCount
    private fun rowToVisitCount(row : ResultRow?) : VisitCount?{
        if(row==null)
        {
            return null
        }

        return VisitCount(
            id = row[VisitCountTable.id],
            sem = row[VisitCountTable.sem],
            count = row[VisitCountTable.count]
        )
    }

}