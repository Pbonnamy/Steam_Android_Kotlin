package com.example.steamlike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private var game: GameDetailsResponse? = null
    private var comments: List<CommentResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

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

                this.setGameDescription()
            }
        }

        this.commentsBtn?.setOnClickListener {
            if (this.commentsList == null) {
                if (this.description != null) {
                    this.layout?.removeView(this.description)
                    this.description = null
                }
                this.setCommentsList(gameId!!)
            }
        }

        this.setGameContent(gameId!!, token!!)
    }

    private fun setGameContent(id: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.gameDetails(id, token)

                if (response.isSuccessful && response.body() != null) {
                    game = response.body()

                    title?.text = game!!.gameDetails.name
                    editor?.text = game!!.gameDetails.editor
                    Glide.with(this@GameActivity).load(game!!.gameDetails.urlImage[0]).into(bannerBackground!!)
                    Glide.with(this@GameActivity).load(game!!.gameDetails.cover).into(bannerImage!!)
                    Glide.with(this@GameActivity).load(game!!.gameDetails.urlImage[1]).into(mainBackground!!)

                    this@GameActivity.setGameDescription()
                    this@GameActivity.handleAppBarBtn(id, token)
                } else {
                    Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setGameDescription() {
        var description = TextView(this)
        description.id = ViewCompat.generateViewId()
        description.text = HtmlCompat.fromHtml(game!!.gameDetails.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
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
        if (game!!.favorite) {
            this.likeBtn?.setBackgroundResource(R.drawable.like_full)
        } else {
            this.likeBtn?.setBackgroundResource(R.drawable.like)
        }

        if (game!!.favorite) {
            this.wishlistBtn?.setBackgroundResource(R.drawable.whishlist_full)
        } else {
            this.wishlistBtn?.setBackgroundResource(R.drawable.whishlist)
        }

        this.likeBtn?.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    lateinit var response: Response<GameResponse>;

                    if (game!!.favorite) {
                        // Remove from favorite
                    } else {
                        response = ApiClient.apiService.addLikeGame(gameId, token)
                    }

                    if (response.isSuccessful && response.body() != null) {
                        if (game!!.favorite) {
                            game!!.favorite = false
                            likeBtn?.setBackgroundResource(R.drawable.like)
                        } else {
                            game!!.favorite = true
                            likeBtn?.setBackgroundResource(R.drawable.like_full)
                        }
                    } else {
                        Log.d("ERROR", response.errorBody().toString())
                        Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.message.toString())
                    Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
                }
            }
        }

        this.wishlistBtn?.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    lateinit var response: Response<GameResponse>;

                    if (game!!.wishList) {
                        // TODO: remove from wishlist
                    } else {
                        response = ApiClient.apiService.addWishlistGame(gameId, token)
                    }

                    if (response.isSuccessful && response.body() != null) {
                        if (game!!.wishList) {
                            game!!.wishList = false
                            wishlistBtn?.setBackgroundResource(R.drawable.whishlist)
                        } else {
                            game!!.wishList = true
                            wishlistBtn?.setBackgroundResource(R.drawable.whishlist_full)
                        }

                    } else {
                        Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setCommentsList(id: String) {
        val commentsList = RecyclerView(this)
        commentsList.id = ViewCompat.generateViewId()
        this.layout?.addView(commentsList)
        this.commentsList = commentsList

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.gameReviews(id)

                if (response.isSuccessful && response.body() != null) {
                    comments = response.body()

                    findViewById<RecyclerView>(commentsList.id).apply {
                        layoutManager = LinearLayoutManager(this@GameActivity)
                        adapter = CommentListView.ListAdapter(comments!!)
                    }
                } else {
                    Toast.makeText(this@GameActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}