package com.example.steamlike

import android.content.Intent
import android.content.SharedPreferences
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamlike.api.model.response.GameResponse

class GameListView {
    class ListAdapter(private val games: List<GameResponse>) : RecyclerView.Adapter<GameViewHolder>() {
        override fun getItemCount(): Int = games.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
            return GameViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.game_item, parent, false
                )
            )
        }
        override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
            holder.updateList(
                games[position]
            )
        }
    }

    class GameViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val title = v.findViewById<TextView>(R.id.title)
        private val editor = v.findViewById<TextView>(R.id.editor)
        private val price = v.findViewById<TextView>(R.id.price)
        private val image = v.findViewById<ImageView>(R.id.image)
        private val background = v.findViewById<ImageView>(R.id.background)
        private val currentContext = v.context
        private val moreInformationsBtn = v.findViewById<Button>(R.id.moreInformationsBtn)
        fun updateList(game: GameResponse) {
            title.text = game.name
            editor.text = game.editor

            val priceString = currentContext.getString(R.string.price, game.price)
            val spannable = SpannableString(priceString)
            spannable.setSpan(UnderlineSpan(), 0, priceString.indexOf(":") - 1, 0)
            price.text = spannable

            Glide.with(currentContext).load(game.urlImage[0]).into(background)
            Glide.with(currentContext).load(game.cover).into(image)

            moreInformationsBtn.setOnClickListener {
                val prefs: SharedPreferences = currentContext.getSharedPreferences("values", AppCompatActivity.MODE_PRIVATE)
                prefs.edit().putString("gameId", game.steamId).apply()
                Log.d("GameListView", "Game id: ${game.steamId}")

                val intent = Intent(currentContext, GameActivity::class.java)
                currentContext.startActivity(intent)
            }
        }
    }
}