package com.example.steamlike.api.model.response

data class GameDetailsResponse(
    val gameDetails: DetailsResponse,
    var wishList: Boolean,
    var favorite: Boolean
)

data class DetailsResponse(
    val name: String,
    val editor: String,
    val urlImage: List<String>,
    val cover: String,
    val description: String,
    val price: String,
    val id: Int
)