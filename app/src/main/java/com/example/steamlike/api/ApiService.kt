package com.example.steamlike.api

import com.example.steamlike.api.model.request.UserSigninRequest
import com.example.steamlike.api.model.request.UserSignupRequest
import com.example.steamlike.api.model.response.ListResponse
import com.example.steamlike.api.model.response.UserSigninResponse
import com.example.steamlike.api.model.response.UserSignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/signup")
   suspend fun authSignup(@Body user: UserSignupRequest): Response<UserSignupResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    suspend fun  authSignin(@Body user: UserSigninRequest): Response<UserSigninResponse>

    @Headers("Content-Type: application/json")
    @GET("steam/search/{term}")
    suspend fun searchSteam(@Path("term") term : String): Response<List<ListResponse>>

    @Headers("Content-Type: application/json")
    @POST("list/save/like/{steamId}")
    suspend fun addLikeGame(@Path("steamId") steamId: Int): Response<List<ListResponse>>
}