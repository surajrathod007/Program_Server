package com.example.data.table

import org.jetbrains.exposed.sql.Table

object UpdateTable : Table() {
    val id = integer("id")
    val version = float("version")
    val link = text("link")
    val message = text("message")

    override val primaryKey: PrimaryKey = PrimaryKey(UpdateTable.id)
}