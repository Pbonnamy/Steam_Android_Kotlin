package com.example.steamlike.api

import com.example.steamlike.api.model.request.PasswordLostRequest
import com.example.steamlike.api.model.request.UserSigninRequest
import com.example.steamlike.api.model.request.UserSignupRequest
import com.example.steamlike.api.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/signup/{lang}")
    suspend fun authSignup(@Body user: UserSignupRequest, @Path("lang") lang: String): Response<UserSignupResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    suspend fun  authSignin(@Body user: UserSigninRequest): Response<UserSigninResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/lost")
    suspend fun lostPassword(@Body request : PasswordLostRequest): Response<PasswordLostResponse>

    @GET("steam/GetMostPlayedGames/{lang}/0/15")
    suspend fun bestGameSells(@Path("lang") lang: String): Response<List<GameResponse>>

    @GET("steam/details/{id}/{lang}")
    @Headers("Content-Type: application/json")
    suspend fun gameDetails(@Path("id") id: String, @Path("lang") lang: String): Response<GameResponse>

    @GET("steam/details/user/game/{id}")
    @Headers("Content-Type: application/json")
    suspend fun userGameDetails(@Path("id") id: String, @Header("Authorization") auth: String): Response<GameDetailsResponse>

    @GET("steam/reviews/{lang}/{id}/0/10")
    suspend fun gameReviews(@Path("id") id: String, @Path("lang") lang: String): Response<List<CommentResponse>>

    @GET("list/all/likelist")
    suspend fun listLikes(@Header("Authorization") auth: String): Response<List<GameResponse>>

    @GET("list/all/wishlist")
    suspend fun listWishlist(@Header("Authorization") auth: String): Response<List<GameResponse>>

    @Headers("Content-Type: application/json")
    @POST("list/save/like/{id}/{lang}")
    suspend fun addLikeGame(@Path("id") id: String, @Header("Authorization")  auth: String, @Path("lang") lang: String): Response<GameResponse>

    @Headers("Content-Type: application/json")
    @POST("list/save/wishlist/{id}/{lang}")
    suspend fun addWishlistGame(@Path("id") id: String, @Header("Authorization") auth: String, @Path("lang") lang: String): Response<GameResponse>

    @DELETE("list/delete/{id}")
    suspend fun removeGame(@Path("id") id: String, @Header("Authorization")  auth: String): Response<Void>

    @Headers("Content-Type: application/json")
    @GET("steam/search/{lang}/{term}")
    suspend fun searchGame(@Path("term") term : String, @Path("lang") lang: String): Response<List<GameResponse>>
}