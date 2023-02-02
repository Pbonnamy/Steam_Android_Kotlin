package com.example.steamlike.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamlike.listview.CommentListView
import com.example.steamlike.LoginActivity
import com.example.steamlike.R
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.response.GameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GameFragment : Fragment() {
    private var progressBarTop: ProgressBar? = null
    private var progressBarBottom: ProgressBar? = null
    private var noItem: TextView? = null
    private var descriptionBtn: Button? = null
    private var commentsBtn: TextView? = null
    private var commentsList: RecyclerView? = null
    private var game: GameResponse? = null
    private var layout : LinearLayout? = null
    private var descriptionTxt: TextView? = null
    private var mainBackground: ImageView? = null
    private var bannerBackground : ImageView? = null
    private var title: TextView? = null
    private var editor: TextView? = null
    private var bannerImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.mainBackground = view.findViewById(R.id.mainBackground)
        this.bannerBackground = view.findViewById(R.id.bannerBackground)
        this.title = view.findViewById(R.id.title)
        this.editor = view.findViewById(R.id.editor)
        this.bannerImage = view.findViewById(R.id.bannerImage)
        this.descriptionBtn = view.findViewById(R.id.descriptionBtn)
        this.commentsBtn = view.findViewById(R.id.commentsBtn)
        this.layout = view.findViewById(R.id.layout)
        this.progressBarTop = view.findViewById(R.id.progressBarTop)
        this.progressBarBottom = view.findViewById(R.id.progressBarBottom)
        this.noItem = view.findViewById(R.id.noItem)

        val sharedPref = requireActivity().getSharedPreferences("values", AppCompatActivity.MODE_PRIVATE)
        val gameId = sharedPref.getString("gameId", null)

        if (gameId == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        this.descriptionBtn?.setOnClickListener {
            if (this.descriptionTxt == null) {

                if (this.commentsList != null) {
                    this.layout?.removeView(this.commentsList)
                    this.commentsList = null
                }

                this.commentsBtn?.background = ContextCompat.getDrawable(requireContext(),
                    R.drawable.bordered_button_primary
                )
                this.descriptionBtn?.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.primary
                ))

                this.setGameDescription()
            }
        }

        this.commentsBtn?.setOnClickListener {
            if (this.commentsList == null) {
                if (this.descriptionTxt != null) {
                    this.layout?.removeView(this.descriptionTxt)
                    this.descriptionTxt = null
                }

                this.descriptionBtn?.background = ContextCompat.getDrawable(requireContext(),
                    R.drawable.bordered_button_primary
                )
                this.commentsBtn?.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.primary
                ))

                this.setCommentsList(gameId!!)
            }
        }

        this.setGameContent(gameId!!)
    }

    private fun setGameContent(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBarBottom?.visibility = ProgressBar.VISIBLE
                progressBarTop?.visibility = ProgressBar.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.gameDetails(id, Locale.getDefault().getLanguage()) }

                if (response.isSuccessful && response.body() != null) {
                    game = response.body()

                    progressBarBottom?.visibility = ProgressBar.GONE
                    progressBarTop?.visibility = ProgressBar.GONE

                    title?.text = game!!.name
                    editor?.text = game!!.editor
                    Glide.with(requireActivity()).load(game!!.urlImage[0]).into(bannerBackground!!)
                    Glide.with(requireActivity()).load(game!!.cover).into(bannerImage!!)
                    Glide.with(requireActivity()).load(game!!.urlImage[1]).into(mainBackground!!)

                    setGameDescription()
                } else {
                    progressBarBottom?.visibility = ProgressBar.GONE
                    progressBarTop?.visibility = ProgressBar.GONE
                    Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarBottom?.visibility = ProgressBar.GONE
                progressBarTop?.visibility = ProgressBar.GONE
                Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setGameDescription() {
        if (game!!.description.isEmpty()) {
            noItem?.text = getString(R.string.noDescription)
            noItem?.visibility = TextView.VISIBLE
        } else {
            noItem?.visibility = TextView.GONE
        }

        val description = TextView(context)
        description.id = ViewCompat.generateViewId()
        description.text = HtmlCompat.fromHtml(game!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.proxima_regular)
        description.setTypeface(typeface)
        description.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        this.layout?.addView(description)
        this.descriptionTxt = description
    }

    private fun setCommentsList(id: String) {
        val commentsContainer = RecyclerView(requireContext())
        commentsContainer.id = ViewCompat.generateViewId()
        this.layout?.addView(commentsContainer)
        this.commentsList = commentsContainer

        CoroutineScope(Dispatchers.Main).launch {
            try {
                noItem?.visibility = TextView.GONE
                progressBarBottom?.visibility = ProgressBar.VISIBLE
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.gameReviews(id, Locale.getDefault().getLanguage()) }

                if (response.isSuccessful && response.body() != null) {
                    val comments = response.body()
                    progressBarBottom?.visibility = ProgressBar.GONE

                    if (comments!!.isNotEmpty()) {
                        commentsList?.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = CommentListView.ListAdapter(comments)
                        }
                    } else {
                        noItem?.text = getString(R.string.noComments)
                        noItem?.visibility = TextView.VISIBLE
                    }
                } else {
                    progressBarBottom?.visibility = ProgressBar.GONE
                    Toast.makeText(activity, getString(R.string.responseError), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBarBottom?.visibility = ProgressBar.GONE
                Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
            }
        }
    }
}