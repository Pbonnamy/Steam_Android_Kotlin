package com.example.steamlike.api.model.response

data class GameDetailsResponse(
    val gameDetails: DetailsResponse,
    val wishList: Boolean,
    val favorite: Boolean
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