package com.example.data.table

import org.jetbrains.exposed.sql.Table

object ProgramTable : Table() {


    val id = integer("id").autoIncrement()
    val title = text("title")
    val content = text("content")
    val sem = varchar("sem",512)
    val sub = varchar("sub",512)
    val unit = varchar("unit",512)

    override val primaryKey: PrimaryKey = PrimaryKey(id)



}