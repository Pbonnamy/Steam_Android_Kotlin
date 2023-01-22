package com.example.steamlike.api.model.response

data class GameResponse(
    val id: String,
    val name: String,
    val editor: String,
    val urlImage: List<String>,
    val cover: String,
    val description: String,
    val price : String
)

