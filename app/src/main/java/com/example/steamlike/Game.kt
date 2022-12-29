package com.example.steamlike

data class Game (
    val title: String,
    val description: String,
    val image: Int,
    val editor: String,
    val price: Double,
    val backgrounds: List<Int>,
)