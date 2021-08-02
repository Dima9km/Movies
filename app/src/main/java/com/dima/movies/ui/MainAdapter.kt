package com.dima.movies.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dima.movies.R
import com.dima.movies.model.Movie
import com.dima.movies.ui.listener.OnFavoriteClickListener
import com.squareup.picasso.Picasso

class MainAdapter(val onFavoriteClickListener: OnFavoriteClickListener) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var movies = mutableListOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false),
            onFavoriteClickListener
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    class MainViewHolder(itemView: View, private val onFavoriteClickListener: OnFavoriteClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var desc: TextView? = null
        private var releaseDate: TextView? = null
        private var favorite: ImageView? = null

        init {
            title = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.ivImage)
            desc = itemView.findViewById(R.id.tvDescription)
            releaseDate = itemView.findViewById(R.id.tvDate)
            favorite = itemView.findViewById(R.id.ivFavorite)
        }

        fun bind(movie: Movie) {

            title?.text = movie.title
            desc?.text = movie.overview
            releaseDate?.text = movie.releaseDate
            Picasso.with(itemView.context)
                .load(movie.posterPath)
                .error(R.drawable.ic_no_image)
                .into(image)

            favorite?.setOnClickListener {
                if (!movie.isFavorite) {
                    movie.isFavorite = true
                    favorite!!.setImageResource(R.drawable.ic_heart_fill)
                    showToast("Добавлено в избранное")
                    onFavoriteClickListener.onClickFavorite(movie)
                } else {
                    movie.isFavorite = false
                    favorite!!.setImageResource(R.drawable.ic_heart)
                    showToast("Удалено из избранного")
                    onFavoriteClickListener.onClickFavorite(movie)
                }
            }
        }

        private fun showToast(text: String) {
            Toast.makeText(itemView.context, text, Toast.LENGTH_LONG).show()
        }
    }
}
