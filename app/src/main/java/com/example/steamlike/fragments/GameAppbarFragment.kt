package com.example.steamlike.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.steamlike.LoginActivity
import com.example.steamlike.MainActivity
import com.example.steamlike.R
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.response.GameDetailsResponse
import com.example.steamlike.api.model.response.GameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GameAppbarFragment : Fragment() {
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null
    private var gameDetails: GameDetailsResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_appbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.appbarTitle = view.findViewById(R.id.appbarTitle)
        this.likeBtn = view.findViewById(R.id.likeBtn)
        this.wishlistBtn = view.findViewById(R.id.wishlistBtn)
        this.leftBtn = view.findViewById(R.id.leftBtn)

        val sharedPref = requireActivity().getSharedPreferences("values", AppCompatActivity.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        val gameId = sharedPref.getString("gameId", null)

        if (gameId == null) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        handleAppBar(gameId!!, token!!)
    }

    private fun handleAppBar(gameId: String, token: String) {
        this.leftBtn?.setBackgroundResource(R.drawable.back)
        this.appbarTitle?.text = getString(R.string.detailsTitle)

        this.leftBtn?.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                likeBtn?.visibility = ImageButton.GONE
                wishlistBtn?.visibility = ImageButton.GONE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.userGameDetails(gameId, token) }

                if (response.isSuccessful && response.body() != null) {
                    gameDetails = response.body()

                    if (gameDetails!!.like) {
                        likeBtn?.setBackgroundResource(R.drawable.like_full)
                    } else {
                        likeBtn?.setBackgroundResource(R.drawable.like)
                    }

                    if (gameDetails!!.wishList) {
                        wishlistBtn?.setBackgroundResource(R.drawable.whishlist_full)
                    } else {
                        wishlistBtn?.setBackgroundResource(R.drawable.whishlist)
                    }

                    handlelikeBtn(token, gameId)
                    handleWishlistBtn(token, gameId)
                } else {
                    Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleWishlistBtn(token: String, gameId: String) {
        wishlistBtn?.visibility = ImageButton.VISIBLE
        this.wishlistBtn?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    wishlistBtn?.isEnabled = false
                    if (gameDetails!!.wishList) {
                        unWishList(gameDetails!!.idWishList!!, token)
                    } else {
                        wishList(gameId, token)
                    }
                    wishlistBtn?.isEnabled = true
                } catch (e: Exception) {
                    Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handlelikeBtn(token: String, gameId: String) {
        likeBtn?.visibility = ImageButton.VISIBLE

        this.likeBtn?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    likeBtn?.isEnabled = false
                    if (gameDetails!!.like) {
                        unLike(gameDetails!!.idLike!!, token)
                    } else {
                        like(gameId, token)
                    }
                    likeBtn?.isEnabled = true
                } catch (e: Exception) {
                    Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun wishList (gameId: String, token: String) {
        val response: Response<GameResponse> = withContext(Dispatchers.IO) { ApiClient.apiService.addWishlistGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.wishList = true
            wishlistBtn?.setBackgroundResource(R.drawable.whishlist_full)
        } else {
            Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun unWishList (gameId: String, token: String) {
        val response: Response<Void> = withContext(Dispatchers.IO) { ApiClient.apiService.removeGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.wishList = false
            wishlistBtn?.setBackgroundResource(R.drawable.whishlist)
        } else {
            Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun like (gameId: String, token: String) {
        val response: Response<GameResponse> = withContext(Dispatchers.IO) { ApiClient.apiService.addLikeGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.like = true
            likeBtn?.setBackgroundResource(R.drawable.like_full)
        } else {
            Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun unLike (gameId: String, token: String) {
        val response: Response<Void> = withContext(Dispatchers.IO) { ApiClient.apiService.removeGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.like = false
            likeBtn?.setBackgroundResource(R.drawable.like)
        } else {
            Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
        }
    }
}