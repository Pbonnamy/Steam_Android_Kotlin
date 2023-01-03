package com.example.steamlike.api.model.request

data class UserSignupRequest(
    val username: String,
    val email: String,
    val roles: List<String>,
    val password: String,

)
