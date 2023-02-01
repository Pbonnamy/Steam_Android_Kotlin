package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.api.ApiClient
import kotlinx.coroutines.*

class LikeActivity : AppCompatActivity() {
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.like_activity)

        this.appbarTitle = findViewById(R.id.appbarTitle)
        this.likeBtn = findViewById(R.id.likeBtn)
        this.wishlistBtn = findViewById(R.id.wishlistBtn)
        this.leftBtn = findViewById(R.id.leftBtn)
        this.progressBar = findViewById(R.id.progressBar)

        val sharedPref = getSharedPreferences("values", MODE_PRIVATE)
        var token = sharedPref.getString("token", null)

        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        this.handleAppBar()
        this.loadLikes(token!!);
    }

    private fun handleAppBar () {
        this.likeBtn?.visibility = View.GONE
        this.wishlistBtn?.visibility = View.GONE
        this.appbarTitle?.text = getString(R.string.likeTitle)
        this.leftBtn?.setBackgroundResource(R.drawable.close)

        this.leftBtn?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadLikes(token : String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBar?.visibility = View.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.listLikes(token) }

                if (response.isSuccessful && response.body() != null) {
                    val games = response.body()
                    val size = games?.size

                    progressBar?.visibility = View.GONE
                    if (size!! > 0) {
                        findViewById<RecyclerView>(R.id.list).apply {
                            layoutManager = LinearLayoutManager(this@LikeActivity)
                            adapter = GameListView.ListAdapter(games!!)
                        }
                    } else {
                        val emptyList = findViewById<ConstraintLayout>(R.id.noItems)
                        emptyList.visibility = View.VISIBLE
                    }
                } else {
                    progressBar?.visibility = View.GONE
                    Toast.makeText(this@LikeActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@LikeActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}