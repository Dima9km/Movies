package com.dima.movies.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dima.movies.R
import com.dima.movies.model.Movie
import com.squareup.picasso.Picasso

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var movies = mutableListOf<Movie>()

    fun setMoviesList(movies: List<Movie>) {
        this.movies = movies.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title: TextView? = null
        private var image: ImageView? = null
        private var desc: TextView? = null
        private var releaseDate: TextView? = null

        init {
            title = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.ivImage)
            desc = itemView.findViewById(R.id.tvDescription)
            releaseDate = itemView.findViewById(R.id.tvDate)
        }

        fun bind(movie: Movie) {
            title?.text = movie.title
            desc?.text = movie.overview
            releaseDate?.text = movie.releaseDate
            Picasso.with(itemView.context)
                .load(movie.posterPath)
                .into(image)
        }
    }
}