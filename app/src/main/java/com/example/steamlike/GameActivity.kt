package com.example.steamlike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.steamlike.fragments.GameAppbarFragment
import com.example.steamlike.fragments.GameFragment

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportFragmentManager.beginTransaction()
            .replace(R.id.appbar, GameAppbarFragment())
            .replace(R.id.gameFragment, GameFragment())
            .commit()
    }
}