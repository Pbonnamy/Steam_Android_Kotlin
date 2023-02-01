package com.example.steamlike

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.appbar, MainAppbarFragment())
            .replace(R.id.banner, MainBannerFragment())
            .replace(R.id.bestSales, BestSalesFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.searchContainer, SearchFragment())
            .commit()

    }
}