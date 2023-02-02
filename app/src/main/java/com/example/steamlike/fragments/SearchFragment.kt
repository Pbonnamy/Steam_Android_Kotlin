package com.example.steamlike.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.listview.GameListView
import com.example.steamlike.R
import com.example.steamlike.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {
    private var searchInput : EditText? = null
    private var appbarTitle: TextView? = null
    private var likeBtn : ImageButton? = null
    private var wishlistBtn : ImageButton? = null
    private var leftBtn : ImageButton? = null
    private var bestSales: View? = null
    private var banner: View? = null
    private var searchTitle : TextView? = null
    private var searchList: RecyclerView? = null
    private var progressBarSearch: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.searchInput = view.findViewById(R.id.search)

        this.appbarTitle = requireActivity().findViewById(R.id.appbarTitle)
        this.likeBtn = requireActivity().findViewById(R.id.likeBtn)
        this.wishlistBtn = requireActivity().findViewById(R.id.wishlistBtn)
        this.leftBtn = requireActivity().findViewById(R.id.leftBtn)

        this.bestSales = requireActivity().findViewById(R.id.bestSales)
        this.banner = requireActivity().findViewById(R.id.banner)

        this.searchList = view.findViewById(R.id.searchResult)
        this.searchTitle = view.findViewById(R.id.resultTitle)
        this.progressBarSearch = view.findViewById(R.id.progressBarSearch)

        handleSearchBar()
    }

    private fun handleSearchBar() {
        searchInput?.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                this.appbarTitle?.text = getString(R.string.searchTitle)
                this.leftBtn?.visibility = View.VISIBLE
                this.wishlistBtn?.visibility = View.GONE
                this.likeBtn?.visibility = View.GONE

                this.bestSales?.visibility = View.GONE
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
                        layoutManager = LinearLayoutManager(activity)
                        adapter = GameListView.ListAdapter(content)
                    }

                    progressBarSearch?.visibility = View.GONE
                    searchTitle?.visibility = View.VISIBLE
                    searchList?.visibility = View.VISIBLE
                } else {
                    progressBarSearch?.visibility = View.GONE
                    Toast.makeText(activity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarSearch?.visibility = View.GONE
                Toast.makeText(activity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun closeSearchBar() {
        this.appbarTitle?.text = getString(R.string.landingTitle)
        this.leftBtn?.visibility = View.GONE
        this.wishlistBtn?.visibility = View.VISIBLE
        this.likeBtn?.visibility = View.VISIBLE

        this.banner?.visibility = View.VISIBLE
        this.bestSales?.visibility = View.VISIBLE

        this.searchTitle?.visibility = View.GONE
        this.searchList?.visibility = View.GONE

        this.searchInput?.text?.clear()
    }
}