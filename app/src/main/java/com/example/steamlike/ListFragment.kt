package com.example.steamlike

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.response.GameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ListFragment : Fragment() {
    private var progressBar: ProgressBar? = null
    private var list: RecyclerView? = null
    private var noItems: ConstraintLayout? = null
    private var noItemImg: ImageView? = null
    private var noItemTxt1: TextView? = null
    private var noItemTxt2: TextView? = null
    private var type: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.progressBar = view.findViewById(R.id.progressBar)
        this.list = view.findViewById(R.id.list)
        this.noItems = view.findViewById(R.id.noItems)
        this.noItemImg = view.findViewById(R.id.noItemImg)
        this.noItemTxt1 = view.findViewById(R.id.noItemTxt1)
        this.noItemTxt2 = view.findViewById(R.id.noItemTxt2)

        this.type = arguments?.getString("type")

        val sharedPref = requireActivity().getSharedPreferences("values", AppCompatActivity.MODE_PRIVATE)
        var token = sharedPref.getString("token", null)

        if (token == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        this.loadList(token!!)
    }

    private fun loadList(token : String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBar?.visibility = View.VISIBLE

                lateinit var response: Response<List<GameResponse>>;

                if (type == "wishlist") {
                    response = withContext(Dispatchers.IO) { ApiClient.apiService.listWishlist(token) }
                } else if (type == "like") {
                    response = withContext(Dispatchers.IO) { ApiClient.apiService.listLikes(token) }
                }

                if (response.isSuccessful && response.body() != null) {
                    val games = response.body()
                    val size = games?.size

                    progressBar?.visibility = View.GONE
                    if (size!! > 0) {
                        list?.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = GameListView.ListAdapter(games!!)
                        }
                    } else {
                        noItemTxt1?.text = getString(R.string.noItems)
                        if (type == "wishlist") {
                            noItemImg?.setImageResource(R.drawable.whishlist_full)
                            noItemTxt2?.text = getString(R.string.wishlistHint)
                        } else if (type == "like") {
                            noItemImg?.setImageResource(R.drawable.like_full)
                            noItemTxt2?.text = getString(R.string.likeHint)
                        }
                        noItems?.visibility = View.VISIBLE
                    }
                } else {
                    progressBar?.visibility = View.GONE
                    Toast.makeText(activity, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar?.visibility = View.GONE
                Toast.makeText(activity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}