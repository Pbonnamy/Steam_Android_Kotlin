package com.example.steamlike.api

import com.example.steamlike.api.model.request.PasswordLostRequest
import com.example.steamlike.api.model.request.UserSigninRequest
import com.example.steamlike.api.model.request.UserSignupRequest
import com.example.steamlike.api.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    suspend fun authSignup(@Body user: UserSignupRequest): Response<UserSignupResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    suspend fun  authSignin(@Body user: UserSigninRequest): Response<UserSigninResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/lost")
    suspend fun lostPassword(@Body request : PasswordLostRequest): Response<PasswordLostResponse>

    @GET("steam/GetMostPlayedGames/fr/0/10")
    suspend fun bestGameSells(): Response<List<GameResponse>>

    @GET("steam/details/{id}/fr")
    suspend fun gameDetails(@Path("id") id: String): Response<GameResponse>

    @GET("steam/reviews/en/{id}/0/5")
    suspend fun gameReviews(@Path("id") id: String): Response<List<CommentResponse>>

    @GET("list/all/likelist")
    suspend fun listLikes(@Header("Authorization") auth: String): Response<List<GameResponse>>

    @GET("list/all/whishlist")
    suspend fun listWishlist(@Header("Authorization") auth: String): Response<List<GameResponse>>

    @Headers("Content-Type: application/json")
    @GET("steam/search/{term}")
    suspend fun searchGame(@Path("term") term : String): Response<List<GameResponse>>

    @Headers("Content-Type: application/json")
    @POST("list/save/like/{steamId}")
    suspend fun addLikeGame(@Path("steamId") steamId: Int, @Header("Authorization")  auth: String): Response<GameResponse>
}