package com.example.steamlike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.response.CommentResponse
import com.example.steamlike.api.model.response.GameDetailsResponse
import com.example.steamlike.api.model.response.GameResponse
import kotlinx.coroutines.*
import retrofit2.Response

class GameActivity : AppCompatActivity() {
    private var mainBackground: ImageView? = null
    private var bannerBackground : ImageView? = null
    private var title: TextView? = null
    private var editor: TextView? = null
    private var bannerImage: ImageView? = null
    private var descriptionBtn: Button? = null
    private var commentsBtn: TextView? = null
    private var layout : LinearLayout? = null
    private var description: TextView? = null
    private var commentsList: RecyclerView? = null
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null
    private var game: GameResponse? = null
    private var gameDetails: GameDetailsResponse? = null
    private var comments: List<CommentResponse>? = null
    private var progressBarTop: ProgressBar? = null
    private var progressBarBottom: ProgressBar? = null
    private var noItem: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        this.mainBackground = findViewById(R.id.mainBackground)
        this.bannerBackground = findViewById(R.id.bannerBackground)
        this.title = findViewById(R.id.title)
        this.editor = findViewById(R.id.editor)
        this.bannerImage = findViewById(R.id.bannerImage)
        this.descriptionBtn = findViewById(R.id.descriptionBtn)
        this.commentsBtn = findViewById(R.id.commentsBtn)
        this.layout = findViewById(R.id.layout)
        this.appbarTitle = findViewById(R.id.appbarTitle)
        this.likeBtn = findViewById(R.id.likeBtn)
        this.wishlistBtn = findViewById(R.id.wishlistBtn)
        this.leftBtn = findViewById(R.id.leftBtn)
        this.progressBarTop = findViewById(R.id.progressBarTop)
        this.progressBarBottom = findViewById(R.id.progressBarBottom)
        this.noItem = findViewById(R.id.noItem)

        val sharedPref = this.getSharedPreferences("values", MODE_PRIVATE)
        val gameId = sharedPref.getString("gameId", null)

        if (gameId == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val token = sharedPref.getString("token", null)

        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        this.handleAppBar()

        this.descriptionBtn?.setOnClickListener {
            if (this.description == null) {

                if (this.commentsList != null) {
                    this.layout?.removeView(this.commentsList)
                    this.commentsList = null
                }

                this.commentsBtn?.background = ContextCompat.getDrawable(this, R.drawable.bordered_button_primary)
                this.descriptionBtn?.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))

                this.setGameDescription()
            }
        }

        this.commentsBtn?.setOnClickListener {
            if (this.commentsList == null) {
                if (this.description != null) {
                    this.layout?.removeView(this.description)
                    this.description = null
                }

                this.descriptionBtn?.background = ContextCompat.getDrawable(this, R.drawable.bordered_button_primary)
                this.commentsBtn?.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))

                this.setCommentsList(gameId!!)
            }
        }

        this.setGameContent(gameId!!, token!!)
    }

    private fun setGameContent(id: String, token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBarBottom?.visibility = ProgressBar.VISIBLE
                progressBarTop?.visibility = ProgressBar.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.gameDetails(id) }

                if (response.isSuccessful && response.body() != null) {
                    game = response.body()

                    progressBarBottom?.visibility = ProgressBar.GONE
                    progressBarTop?.visibility = ProgressBar.GONE

                    title?.text = game!!.name
                    editor?.text = game!!.editor
                    Glide.with(this@GameActivity).load(game!!.urlImage[0]).into(bannerBackground!!)
                    Glide.with(this@GameActivity).load(game!!.cover).into(bannerImage!!)
                    Glide.with(this@GameActivity).load(game!!.urlImage[1]).into(mainBackground!!)

                    this@GameActivity.setGameDescription()
                    this@GameActivity.handleAppBarBtn(id, token)
                } else {
                    progressBarBottom?.visibility = ProgressBar.GONE
                    progressBarTop?.visibility = ProgressBar.GONE
                    Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarBottom?.visibility = ProgressBar.GONE
                progressBarTop?.visibility = ProgressBar.GONE
                Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setGameDescription() {
        if (game!!.description.isEmpty()) {
            noItem?.visibility = TextView.VISIBLE
            noItem?.text = getString(R.string.noDescription)
        } else {
            noItem?.visibility = TextView.GONE
        }

        var description = TextView(this)
        description.id = ViewCompat.generateViewId()
        description.text = HtmlCompat.fromHtml(game!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        description.setTextColor(ContextCompat.getColor(this, R.color.white))

        this.layout?.addView(description)
        this.description = description
    }

    private fun handleAppBar () {
        this.leftBtn?.setBackgroundResource(R.drawable.back)
        this.appbarTitle?.text = getString(R.string.detailsTitle)

        this.leftBtn?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleAppBarBtn(gameId: String, token: String) {
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

                    likeBtn?.visibility = ImageButton.VISIBLE
                    wishlistBtn?.visibility = ImageButton.VISIBLE
                } else {
                    Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }

        this.likeBtn?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    likeBtn?.isEnabled = false
                    if (gameDetails!!.like) {
                        unLike(gameDetails!!.idLike!!, token)
                    } else {
                        like(game!!.steamId, token)
                    }
                    likeBtn?.isEnabled = true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
                }
            }
        }

        this.wishlistBtn?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    wishlistBtn?.isEnabled = false
                    if (gameDetails!!.wishList) {
                        unWishList(gameDetails!!.idWishList!!, token)
                    } else {
                        wishList(game!!.steamId, token)
                    }
                    wishlistBtn?.isEnabled = true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun unWishList (gameId: String, token: String) {
        val response: Response<Void> = withContext(Dispatchers.IO) { ApiClient.apiService.removeGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.wishList = false
            wishlistBtn?.setBackgroundResource(R.drawable.whishlist)
        } else {
            Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun like (gameId: String, token: String) {
        val response: Response<GameResponse> = withContext(Dispatchers.IO) { ApiClient.apiService.addLikeGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.like = true
            likeBtn?.setBackgroundResource(R.drawable.like_full)
        } else {
            Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun unLike (gameId: String, token: String) {
        val response: Response<Void> = withContext(Dispatchers.IO) { ApiClient.apiService.removeGame(gameId, token) }

        if (response.isSuccessful) {
            gameDetails!!.like = false
            likeBtn?.setBackgroundResource(R.drawable.like)
        } else {
            Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCommentsList(id: String) {
        val commentsList = RecyclerView(this)
        commentsList.id = ViewCompat.generateViewId()
        this.layout?.addView(commentsList)
        this.commentsList = commentsList

        CoroutineScope(Dispatchers.Main).launch {
            try {
                noItem?.visibility = TextView.GONE
                progressBarBottom?.visibility = ProgressBar.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.gameReviews(id) }

                if (response.isSuccessful && response.body() != null) {
                    comments = response.body()
                    progressBarBottom?.visibility = ProgressBar.GONE

                    if (comments!!.size > 0) {
                        findViewById<RecyclerView>(commentsList.id).apply {
                            layoutManager = LinearLayoutManager(this@GameActivity)
                            adapter = CommentListView.ListAdapter(comments!!)
                        }
                    } else {
                        noItem?.visibility = TextView.VISIBLE
                    }
                } else {
                    progressBarBottom?.visibility = ProgressBar.GONE
                    Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarBottom?.visibility = ProgressBar.GONE
                Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}