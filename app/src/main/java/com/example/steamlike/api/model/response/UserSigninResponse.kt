package com.example.steamlike.api.model.response

data class UserSigninResponse (
    val id: String,
    val username: String,
    val email: String,
    val roles: List<String>,
    val accessToken: String,
    val tokenType: String,
)