package com.example.steamlike.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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

class BestSalesFragment: Fragment() {
    private var saleList: RecyclerView? = null
    private var progressBarSale: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_best_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.saleList = view.findViewById(R.id.list)
        this.progressBarSale = view.findViewById(R.id.progressBarSale)

        this.setBestSalesContent()
    }

    private fun setBestSalesContent() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBarSale?.visibility = View.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.bestGameSells() }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    progressBarSale?.visibility = View.GONE
                    saleList?.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = GameListView.ListAdapter(content!!)
                    }
                } else {
                    progressBarSale?.visibility = View.GONE
                    Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarSale?.visibility = View.GONE
                Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
            }
        }
    }
}