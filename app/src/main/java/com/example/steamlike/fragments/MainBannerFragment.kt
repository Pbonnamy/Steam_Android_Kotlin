package com.example.steamlike.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.steamlike.GameActivity
import com.example.steamlike.R
import com.example.steamlike.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainBannerFragment: Fragment() {
    private var progressBarBanner: ProgressBar? = null
    private var bannerTitle: TextView? = null
    private var bannerDescription: TextView? = null
    private var bannerImage: ImageView? = null
    private var bannerBackground: ImageView? = null
    private var moreInformationsBtn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_main_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.moreInformationsBtn = view.findViewById(R.id.moreInformationsBtn)
        this.bannerTitle = view.findViewById(R.id.bannerTitle)
        this.bannerDescription = view.findViewById(R.id.bannerDescription)
        this.bannerImage = view.findViewById(R.id.bannerImage)
        this.bannerBackground = view.findViewById(R.id.bannerBackground)
        this.progressBarBanner = view.findViewById(R.id.progressBarBanner)

        this.setBannerContent()
    }

    private fun setBannerContent() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                moreInformationsBtn?.visibility = View.GONE
                progressBarBanner?.visibility = View.VISIBLE
                val letter = ('a'..'z').random()
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.searchGame(letter.toString(), Locale.getDefault().getLanguage()) }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    moreInformationsBtn?.setOnClickListener {
                        val prefs: SharedPreferences = requireActivity().getSharedPreferences("values",
                            AppCompatActivity.MODE_PRIVATE
                        )
                        prefs.edit().putString("gameId", content!![0].steamId).apply()

                        val intent = Intent(activity, GameActivity::class.java)
                        startActivity(intent)
                    }

                    moreInformationsBtn?.visibility = View.VISIBLE
                    progressBarBanner?.visibility = View.GONE

                    bannerTitle?.text = content!![0].name
                    bannerDescription?.text = HtmlCompat.fromHtml(content[0].description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    Glide.with(requireActivity()).load(content[0].cover).into(bannerImage!!)
                    Glide.with(requireActivity()).load(content[0].urlImage[0]).into(bannerBackground!!)
                } else {
                    progressBarBanner?.visibility = View.GONE
                    Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarBanner?.visibility = View.GONE
                Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
            }
        }
    }
}