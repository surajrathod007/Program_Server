package com.example.data.table

import org.jetbrains.exposed.sql.Table

object VisitCountTable : Table(){

    val id = integer("id")
    val sem = text("sem")
    val count = integer("count")

    override val primaryKey: PrimaryKey = PrimaryKey(VisitCountTable.id)

}