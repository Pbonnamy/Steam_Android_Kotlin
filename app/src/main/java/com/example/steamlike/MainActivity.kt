package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.UserSignupRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var moreInformationsBtn: Button? = null
    private var bannerTitle: TextView? = null
    private var bannerDescription: TextView? = null
    private var bannerImage: ImageView? = null
    private var bannerLayout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        this.moreInformationsBtn = findViewById(R.id.moreInformationsBtn)
        this.bannerTitle = findViewById(R.id.bannerTitle)
        this.bannerDescription = findViewById(R.id.bannerDescription)
        this.bannerImage = findViewById(R.id.bannerImage)
        this.bannerLayout = findViewById(R.id.bannerLayout)

        this.moreInformationsBtn?.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

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
        val games = mutableListOf<Game>();

        val game = Game(
            "Nom du jeu",
            "",
            R.drawable.test_game2,
            "Nom de l'éditeur",
            10.00,
            listOf(R.drawable.test_banner2)
        )

        for (i in 0..5) {
            games.add(game)
        }

        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter(games)
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