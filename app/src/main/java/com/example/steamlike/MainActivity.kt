package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.api.ApiClient
import kotlinx.coroutines.*


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
    private var searchInput : EditText? = null
    private var saleTitle : TextView? = null
    private var searchTitle : TextView? = null
    private var saleList: RecyclerView? = null
    private var searchList: RecyclerView? = null

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
        this.searchInput = findViewById(R.id.search)
        this.saleTitle = findViewById(R.id.bestSalesTitle)
        this.saleList = findViewById(R.id.list)
        this.searchList = findViewById(R.id.searchResult)
        this.searchTitle = findViewById(R.id.resultTitle)

        this.moreInformationsBtn?.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        this.handleAppBar()
        this.setBannerContent()
        this.setBestSalesContent()
        this.handleSearchBar()
    }

    private fun setBannerContent() {
        this.bannerTitle?.text = "Titan Fall 2\nUltimate Edition"
        this.bannerDescription?.text = "Une description d'un jeu mis en avant (peu être fait en dur)"
        this.bannerImage?.setImageResource(R.drawable.test_game1)
        this.bannerLayout?.setBackgroundResource(R.drawable.test_banner1)
    }

    private fun handleSearchBar() {
        searchInput?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                this.appbarTitle?.text = getString(R.string.searchTitle)
                this.leftBtn?.visibility = View.VISIBLE

                this.wishlistBtn?.visibility = View.GONE
                this.likeBtn?.visibility = View.GONE
                this.bannerLayout?.visibility = View.GONE
                this.saleList?.visibility = View.GONE
                this.saleTitle?.visibility = View.GONE

                val searchTxt = searchInput?.text.toString()
                search(searchTxt)

                this.leftBtn?.setOnClickListener {
                    this.closeSearchBar()
                }

                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun closeSearchBar() {
        this.appbarTitle?.text = getString(R.string.landingTitle)
        this.leftBtn?.visibility = View.GONE

        this.wishlistBtn?.visibility = View.VISIBLE
        this.likeBtn?.visibility = View.VISIBLE
        this.bannerLayout?.visibility = View.VISIBLE
        this.saleList?.visibility = View.VISIBLE
        this.saleTitle?.visibility = View.VISIBLE

        this.searchTitle?.visibility = View.GONE
        this.searchList?.visibility = View.GONE

        this.searchInput?.text?.clear()
    }

    private fun search(term: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.searchGame(term) }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    val searchString = getString(R.string.searchResult, content!!.size.toString())
                    val spannable = SpannableString(searchString)
                    spannable.setSpan(UnderlineSpan(), 0, searchString.indexOf(":") - 1, 0)
                    searchTitle?.text = spannable

                    searchList?.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = GameListView.ListAdapter(content!!)
                    }

                    searchTitle?.visibility = View.VISIBLE
                    searchList?.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBestSalesContent() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.bestGameSells() }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    saleList?.apply {
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