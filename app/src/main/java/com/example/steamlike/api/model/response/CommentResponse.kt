package com.example.steamlike.api.model.response

data class CommentResponse (
    val id : String,
    val name : String,
    val like : Int,
    val description : String,
)