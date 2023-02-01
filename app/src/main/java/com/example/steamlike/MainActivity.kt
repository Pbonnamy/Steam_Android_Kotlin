package com.example.steamlike

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamlike.api.ApiClient
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private var moreInformationsBtn: Button? = null
    private var bannerTitle: TextView? = null
    private var bannerDescription: TextView? = null
    private var bannerImage: ImageView? = null
    private var bannerBackground: ImageView? = null
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null
    private var searchInput : EditText? = null
    private var saleTitle : TextView? = null
    private var searchTitle : TextView? = null
    private var saleList: RecyclerView? = null
    private var searchList: RecyclerView? = null
    private var progressBarSearch: ProgressBar? = null
    private var progressBarSale: ProgressBar? = null
    private var progressBarBanner: ProgressBar? = null
    private var banner: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        this.moreInformationsBtn = findViewById(R.id.moreInformationsBtn)
        this.bannerTitle = findViewById(R.id.bannerTitle)
        this.bannerDescription = findViewById(R.id.bannerDescription)
        this.bannerImage = findViewById(R.id.bannerImage)
        this.appbarTitle = findViewById(R.id.appbarTitle)
        this.likeBtn = findViewById(R.id.likeBtn)
        this.wishlistBtn = findViewById(R.id.wishlistBtn)
        this.leftBtn = findViewById(R.id.leftBtn)
        this.searchInput = findViewById(R.id.search)
        this.saleTitle = findViewById(R.id.bestSalesTitle)
        this.saleList = findViewById(R.id.list)
        this.searchList = findViewById(R.id.searchResult)
        this.searchTitle = findViewById(R.id.resultTitle)
        this.progressBarSearch = findViewById(R.id.progressBarSearch)
        this.progressBarSale = findViewById(R.id.progressBarSale)
        this.bannerBackground = findViewById(R.id.bannerBackground)
        this.progressBarBanner = findViewById(R.id.progressBarBanner)
        this.banner = findViewById(R.id.banner)

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
        CoroutineScope(Dispatchers.Main).launch {
            try {
                moreInformationsBtn?.visibility = View.GONE
                progressBarBanner?.visibility = View.VISIBLE
                val letter = ('a'..'z').random()
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.searchGame(letter.toString()) }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    moreInformationsBtn?.setOnClickListener {
                        val prefs: SharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
                        prefs.edit().putString("gameId", content!![0].steamId).apply()

                        val intent = Intent(this@MainActivity, GameActivity::class.java)
                        startActivity(intent)
                    }

                    moreInformationsBtn?.visibility = View.VISIBLE
                    progressBarBanner?.visibility = View.GONE

                    bannerTitle?.text = content!![0].name
                    bannerDescription?.text = HtmlCompat.fromHtml(content[0].description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    Glide.with(this@MainActivity).load(content[0].cover).into(this@MainActivity.bannerImage!!)
                    Glide.with(this@MainActivity).load(content[0].urlImage[0]).into(this@MainActivity.bannerBackground!!)
                } else {
                    progressBarBanner?.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarBanner?.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSearchBar() {
        searchInput?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                this.appbarTitle?.text = getString(R.string.searchTitle)
                this.leftBtn?.visibility = View.VISIBLE

                this.wishlistBtn?.visibility = View.GONE
                this.likeBtn?.visibility = View.GONE
                this.saleList?.visibility = View.GONE
                this.saleTitle?.visibility = View.GONE
                this.banner?.visibility = View.GONE

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
        this.banner?.visibility = View.VISIBLE
        this.saleList?.visibility = View.VISIBLE
        this.saleTitle?.visibility = View.VISIBLE

        this.searchTitle?.visibility = View.GONE
        this.searchList?.visibility = View.GONE

        this.searchInput?.text?.clear()
    }

    private fun search(term: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                searchList?.visibility = View.GONE
                progressBarSearch?.visibility = View.VISIBLE
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

                    progressBarSearch?.visibility = View.GONE
                    searchTitle?.visibility = View.VISIBLE
                    searchList?.visibility = View.VISIBLE
                } else {
                    progressBarSearch?.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarSearch?.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBestSalesContent() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBarSale?.visibility = View.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.bestGameSells() }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    progressBarSale?.visibility = View.GONE
                    saleList?.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = GameListView.ListAdapter(content!!)
                    }
                } else {
                    progressBarSale?.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarSale?.visibility = View.GONE
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