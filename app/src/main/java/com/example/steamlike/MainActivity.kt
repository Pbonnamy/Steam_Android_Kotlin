package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var moreInformationsBtn: Button? = null
    private var bannerTitle: TextView? = null
    private var bannerDescription: TextView? = null
    private var bannerImage: ImageView? = null
    private var bannerLayout: ConstraintLayout? = null
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        this.moreInformationsBtn = findViewById(R.id.moreInformationsBtn)
        this.bannerTitle = findViewById(R.id.bannerTitle)
        this.bannerDescription = findViewById(R.id.bannerDescription)
        this.bannerImage = findViewById(R.id.bannerImage)
        this.bannerLayout = findViewById(R.id.bannerLayout)
        this.appbarTitle = findViewById(R.id.appbarTitle)
        this.likeBtn = findViewById(R.id.likeBtn)
        this.wishlistBtn = findViewById(R.id.wishlistBtn)
        this.leftBtn = findViewById(R.id.leftBtn)

        this.moreInformationsBtn?.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        this.handleAppBar()
        this.setBannerContent()
        this.setBestSalesContent()
    }

    private fun setBannerContent() {
        this.bannerTitle?.text = "Titan Fall 2\nUltimate Edition"
        this.bannerDescription?.text = "Une description d'un jeu mis en avant (peu être fait en dur)"
        this.bannerImage?.setImageResource(R.drawable.test_game1)
        this.bannerLayout?.setBackgroundResource(R.drawable.test_banner1)
    }

    private fun setBestSalesContent() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.bestGameSells()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    findViewById<RecyclerView>(R.id.list).apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = GameListView.ListAdapter(content!!)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAppBar () {
        this.leftBtn?.visibility = View.GONE
        this.appbarTitle?.text = getString(R.string.landingTitle)

        this.likeBtn?.setOnClickListener {
            val intent = Intent(this, LikeActivity::class.java)
            startActivity(intent)
        }

        this.wishlistBtn?.setOnClickListener {
            val intent = Intent(this, WishlistActivity::class.java)
            startActivity(intent)
        }
    }
}