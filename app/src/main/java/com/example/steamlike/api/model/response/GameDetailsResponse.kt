package com.example.steamlike.api.model.response

data class GameDetailsResponse(
    val idLike: String?,
    val idWishlist: String?,
    var like: Boolean,
    var wishList: Boolean
)