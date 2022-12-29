package com.example.steamlike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    private var mainBackground: ImageView? = null
    private var bannerBackground : ImageView? = null
    private var title: TextView? = null
    private var editor: TextView? = null
    private var bannerImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        this.mainBackground = findViewById(R.id.mainBackground)
        this.bannerBackground = findViewById(R.id.bannerBackground)
        this.title = findViewById(R.id.title)
        this.editor = findViewById(R.id.editor)
        this.bannerImage = findViewById(R.id.bannerImage)

        this.setGameContent()
    }

    fun setGameContent() {
        this.mainBackground?.setImageResource(R.drawable.test_banner3)
        this.bannerBackground?.setImageResource(R.drawable.test_banner4)
        this.title?.text = "Nom du jeu"
        this.editor?.text = "Nom de l'éditeur"
        this.bannerImage?.setImageResource(R.drawable.test_game3)
    }
}