package com.example.steamlike

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.steamlike.fragments.BestSalesFragment
import com.example.steamlike.fragments.MainAppbarFragment
import com.example.steamlike.fragments.MainBannerFragment
import com.example.steamlike.fragments.SearchFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("main", Locale.getDefault().getLanguage())

        supportFragmentManager.beginTransaction()
            .replace(R.id.appbar, MainAppbarFragment())
            .replace(R.id.banner, MainBannerFragment())
            .replace(R.id.bestSales, BestSalesFragment())
            .replace(R.id.searchContainer, SearchFragment())
            .commit()
    }
}