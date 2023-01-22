package com.example.steamlike

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.steamlike.api.model.response.CommentResponse

class CommentListView {
    class ListAdapter(private val comments: List<CommentResponse>) : RecyclerView.Adapter<CommentViewHolder>() {

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

        fun updateComment(comment: CommentResponse) {
            var spannableUsername = SpannableString(comment.name)
            spannableUsername.setSpan(UnderlineSpan(), 0, spannableUsername.length, 0)
            username.text = spannableUsername

            content.text = comment.description
        }
    }
}