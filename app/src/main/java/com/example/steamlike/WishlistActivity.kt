package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WishlistActivity : AppCompatActivity() {
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wishlist_activity)

        this.appbarTitle = findViewById(R.id.appbarTitle)
        this.likeBtn = findViewById(R.id.likeBtn)
        this.wishlistBtn = findViewById(R.id.wishlistBtn)
        this.leftBtn = findViewById(R.id.leftBtn)

        this.handleAppBar()
    }

    private fun handleAppBar () {
        this.likeBtn?.visibility = View.GONE
        this.wishlistBtn?.visibility = View.GONE
        this.appbarTitle?.text = getString(R.string.wishlistTitle)
        this.leftBtn?.setBackgroundResource(R.drawable.close)


        this.leftBtn?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}