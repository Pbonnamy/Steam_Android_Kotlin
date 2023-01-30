package com.example.steamlike.api.model.request

data class UserSigninRequest(
    val email: String,
    val password: String,
)
