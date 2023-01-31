package com.example.steamlike.api.model.response

data class GameDetailsResponse(
    val idLike: String?,
    val idWishList: String?,
    var like: Boolean,
    var wishList: Boolean
)