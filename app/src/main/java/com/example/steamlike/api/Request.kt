package com.example.steamlike.api

import android.util.Log
import android.util.Log.INFO
import com.example.steamlike.api.model.request.UserSigninRequest
import com.example.steamlike.api.model.request.UserSignupRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Request {
    private lateinit var TOKEN: String


    fun start() {
        val ROLE = "ROLE_USER"
        val user = UserSignupRequest(
            username = "Pierre",
            email = "pierre@pierre.company",
            roles = listOf(ROLE),
            password = "pierrekkkk"
        )
        signup(user)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun signup(user: UserSignupRequest) {

        GlobalScope.launch(Dispatchers.Main) {
            try {

                val response = ApiClient.apiService.authSignup(user)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    //do something
                    Log.i("info", content.toString())
                    signin(user.username, user.password )
                } else {

                    Log.e("error1", "Error Occurred: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("error1", "Error Occurred: ${e.message}")
                signin(user.username, user.password)
            }
        }


    }

    private fun signin(username: String, password: String){
        val user = UserSigninRequest(
            username = username, password = password
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {

                val response = ApiClient.apiService.authSignin(user)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    //do something
                    if(content!=null){
                        TOKEN = content.accessToken
                        searchGame(term = "b")
                    }

                } else {

                    Log.e("error2", "Error Occurred: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("error2", "Error Occurred: ${e.message}")
            }
        }
    }


    private fun searchGame(term: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.searchSteam(term)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    //do something
                    if (content!=null){
                        Log.i("Game info", content[0].toString())
                        addGameLikeList(content[0].id.toInt())
                    }

                } else {

                    Log.e("error3", "Error Occurred: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("error3", "Error Occurred: ${e.message}")
            }

        }
    }

    private fun addGameLikeList(steamId: Int ){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.addLikeGame(steamId, "Bearer "+TOKEN)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    //do something
                    if (content!=null){
                        Log.i("Game info", content.toString())
                    }

                } else {

                    Log.e("error4", "Error Occurred: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("error4", "Error Occurred: ${e.message}")
            }

        }
    }
}
