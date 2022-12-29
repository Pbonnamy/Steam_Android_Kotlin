package com.example.steamlike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameActivity : AppCompatActivity() {
    private var mainBackground: ImageView? = null
    private var bannerBackground : ImageView? = null
    private var title: TextView? = null
    private var editor: TextView? = null
    private var bannerImage: ImageView? = null
    private var descriptionBtn: Button? = null
    private var commentsBtn: TextView? = null
    private var layout : LinearLayout? = null
    private var description: TextView? = null
    private var commentsList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        this.mainBackground = findViewById(R.id.mainBackground)
        this.bannerBackground = findViewById(R.id.bannerBackground)
        this.title = findViewById(R.id.title)
        this.editor = findViewById(R.id.editor)
        this.bannerImage = findViewById(R.id.bannerImage)
        this.descriptionBtn = findViewById(R.id.descriptionBtn)
        this.commentsBtn = findViewById(R.id.commentsBtn)
        this.layout = findViewById(R.id.layout)

        this.descriptionBtn?.setOnClickListener {
            if (this.description == null) {

                if (this.commentsList != null) {
                    this.layout?.removeView(this.commentsList)
                    this.commentsList = null
                }

                this.setGameDescription()
            }
        }

        this.commentsBtn?.setOnClickListener {
            if (this.commentsList == null) {
                if (this.description != null) {
                    this.layout?.removeView(this.description)
                    this.description = null
                }
                this.setCommentsList()
            }
        }

        this.setGameContent()
        this.setGameDescription()
    }

    private fun setGameContent() {
        this.mainBackground?.setImageResource(R.drawable.test_banner3)
        this.bannerBackground?.setImageResource(R.drawable.test_banner4)
        this.title?.text = "Nom du jeu"
        this.editor?.text = "Nom de l'éditeur"
        this.bannerImage?.setImageResource(R.drawable.test_game3)
    }

    private fun setGameDescription() {
        var description: TextView = TextView(this)
        description.id = ViewCompat.generateViewId()
        description.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        description.setTextColor(ContextCompat.getColor(this, R.color.white))

        this.layout?.addView(description)
        this.description = description
    }

    private fun setCommentsList() {
        var commentsList: RecyclerView = RecyclerView(this)
        commentsList.id = ViewCompat.generateViewId()
        this.layout?.addView(commentsList)
        this.commentsList = commentsList

        val comments = mutableListOf<Comment>();

        val comment = Comment(
            "Nom de l'utilisateur",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            5
        )

        for (i in 0..5) {
            comments.add(comment)
        }

        findViewById<RecyclerView>(commentsList.id).apply {
            layoutManager = LinearLayoutManager(this@GameActivity)
            adapter = ListAdapter(comments)
        }
    }

    class ListAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentViewHolder>() {

        override fun getItemCount(): Int = comments.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
            return CommentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.comment_item, parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
            holder.updateComment(
                comments[position]
            )
        }
    }

    class CommentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val username = v.findViewById<TextView>(R.id.username)
        private val content = v.findViewById<TextView>(R.id.content)

        fun updateComment(comment: Comment) {
            var spannableUsername = SpannableString(comment.username)
            spannableUsername.setSpan(UnderlineSpan(), 0, spannableUsername.length, 0)
            username.text = spannableUsername

            content.text = comment.content
        }
    }
}