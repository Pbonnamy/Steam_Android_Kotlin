package com.example.steamlike.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.steamlike.ListActivity
import com.example.steamlike.R

class MainAppbarFragment: Fragment() {
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_appbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.appbarTitle = view.findViewById(R.id.appbarTitle)
        this.likeBtn = view.findViewById(R.id.likeBtn)
        this.wishlistBtn = view.findViewById(R.id.wishlistBtn)
        this.leftBtn = view.findViewById(R.id.leftBtn)

        handleAppBar()
    }

    private fun handleAppBar () {
        this.leftBtn?.visibility = View.GONE
        this.appbarTitle?.text = getString(R.string.landingTitle)

        this.likeBtn?.setOnClickListener {
            val intent = Intent(activity, ListActivity::class.java).putExtra("type", "like")
            startActivity(intent)
        }

        this.wishlistBtn?.setOnClickListener {
            val intent = Intent(activity, ListActivity::class.java).putExtra("type", "wishlist")
            startActivity(intent)
        }
    }
}