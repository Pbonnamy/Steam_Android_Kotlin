package com.example.steamlike

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.steamlike.fragments.ListAppbarFragment
import com.example.steamlike.fragments.ListFragment

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        val bundleAppbar = Bundle()
        val appbarFragment = ListAppbarFragment()
        val bundleList = Bundle()
        val listFragment = ListFragment()

        if (intent.getStringExtra("type") == "like") {
            bundleAppbar.putString("titleActivity", getString(R.string.likeTitle))
            bundleList.putString("type", "like")
        } else if (intent.getStringExtra("type") == "wishlist") {
            bundleAppbar.putString("titleActivity", getString(R.string.wishlistTitle))
            bundleList.putString("type", "wishlist")
        }

        appbarFragment.arguments = bundleAppbar
        listFragment.arguments = bundleList

        supportFragmentManager.beginTransaction()
            .replace(R.id.appbar, appbarFragment)
            .replace(R.id.listContainer, listFragment)
            .commit()
    }

}