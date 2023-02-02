package com.example.steamlike.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.steamlike.MainActivity
import com.example.steamlike.R

class ListAppbarFragment : Fragment() {
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null
    private var titleActivity: String? = null

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

        this.titleActivity = arguments?.getString("titleActivity")

        handleAppBar()
    }

    private fun handleAppBar () {
        this.likeBtn?.visibility = View.GONE
        this.wishlistBtn?.visibility = View.GONE
        this.appbarTitle?.text = this.titleActivity
        this.leftBtn?.setBackgroundResource(R.drawable.close)

        this.leftBtn?.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}