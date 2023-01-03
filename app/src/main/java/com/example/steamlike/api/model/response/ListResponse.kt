package com.example.steamlike.api.model.response

data class ListResponse(
    val id: String,
    val tenant: String,
    val name: String,
    val steamID: Int,
    val editor: String,
    val urlImage: List<String>,
    val cover: String,
    val description: String,
    val price: String,
    val type: String,
)
