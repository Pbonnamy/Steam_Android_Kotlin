package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LikeActivity : AppCompatActivity() {
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.like_activity)

        this.appbarTitle = findViewById(R.id.appbarTitle)
        this.likeBtn = findViewById(R.id.likeBtn)
        this.wishlistBtn = findViewById(R.id.wishlistBtn)
        this.leftBtn = findViewById(R.id.leftBtn)

        this.handleAppBar()
        this.loadLikes()
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

    private fun loadLikes() {
        val games = mutableListOf<Game>();

        val game = Game(
            "Nom du jeu",
            "",
            R.drawable.test_game2,
            "Nom de l'éditeur",
            10.00,
            listOf(R.drawable.test_banner2)
        )

        for (i in 0..10) {
            games.add(game)
        }

        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(this@LikeActivity)
            adapter = MainActivity.ListAdapter(games)
        }
    }

    class ListAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameViewHolder>() {
        override fun getItemCount(): Int = games.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
            return GameViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.game_item, parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
            holder.updateBooking(
                games[position]
            )
        }
    }

    class GameViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val title = v.findViewById<TextView>(R.id.title)
        private val editor = v.findViewById<TextView>(R.id.editor)
        private val price = v.findViewById<TextView>(R.id.price)
        private val image = v.findViewById<ImageView>(R.id.image)
        private val background = v.findViewById<ImageView>(R.id.background)
        private val currentContext = v.context
        private val moreInformationsBtn = v.findViewById<Button>(R.id.moreInformationsBtn)

        fun updateBooking(game: Game) {
            title.text = game.title
            editor.text = game.editor

            val priceString = currentContext.getString(R.string.price, String.format("%.2f", game.price))
            val spannable = SpannableString(priceString)
            spannable.setSpan(UnderlineSpan(), 0, priceString.indexOf(":") - 1, 0)
            price.text = spannable

            image.setImageResource(game.image)
            background.setImageResource(game.backgrounds[0])

            moreInformationsBtn.setOnClickListener {
                val intent = Intent(currentContext, GameActivity::class.java)
                currentContext.startActivity(intent)
            }
        }
    }
}