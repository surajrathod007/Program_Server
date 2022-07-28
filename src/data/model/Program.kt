package com.example.data.model

import java.util.*

data class Program(

    val id : Int = Random().nextInt(),
    val title : String,
    val content : String,
    val sem : String,
    val sub : String,
    val unit : String
)


/*
val id : Int = Random().nextInt(),
    val title : String,
    val content : String,
    val sem : String,
    val sub : String,
    val unit : String
)
*/