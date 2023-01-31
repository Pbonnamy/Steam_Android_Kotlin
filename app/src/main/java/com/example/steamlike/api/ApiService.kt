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
    @Headers("Content-Type: application/json")
    suspend fun gameDetails(@Path("id") id: String): Response<GameResponse>

    @GET("steam/details/user/game/{id}")
    @Headers("Content-Type: application/json")
    suspend fun userGameDetails(@Path("id") id: String, @Header("Authorization") auth: String): Response<GameDetailsResponse>

    @GET("steam/reviews/en/{id}/0/10")
    suspend fun gameReviews(@Path("id") id: String): Response<List<CommentResponse>>

    @GET("list/all/likelist")
    suspend fun listLikes(@Header("Authorization") auth: String): Response<List<GameResponse>>

    @GET("list/all/wishlist")
    suspend fun listWishlist(@Header("Authorization") auth: String): Response<List<GameResponse>>

    @Headers("Content-Type: application/json")
    @POST("list/save/like/{id}/fr")
    suspend fun addLikeGame(@Path("id") id: String, @Header("Authorization")  auth: String): Response<GameResponse>

    @Headers("Content-Type: application/json")
    @POST("list/save/wishlist/{id}/fr")
    suspend fun addWishlistGame(@Path("id") id: String, @Header("Authorization")  auth: String): Response<GameResponse>

    @DELETE("list/delete/{id}")
    suspend fun removeGame(@Path("id") id: String, @Header("Authorization")  auth: String): Response<Void>

    @Headers("Content-Type: application/json")
    @GET("steam/search/fr/{term}")
    suspend fun searchGame(@Path("term") term : String): Response<List<GameResponse>>
}