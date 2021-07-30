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

    fun setMovieList(movies: List<Movie>) {
        this.movies = movies.toMutableList()
        notifyDataSetChanged()
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
        var title: TextView? = null
        var image: ImageView? = null
        var desc: TextView? = null
        var releaseDate: TextView? = null

        init {
            title = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.ivImage)
            title = itemView.findViewById(R.id.tvTitle)
            title = itemView.findViewById(R.id.tvTitle)

        }

        fun bind(movie: Movie) {
            title?.setText(movie.title)
            Picasso.with(itemView.getContext())
                .load(movie.imageUrl)
                .into(image)
            desc?.setText(movie.desc)
            releaseDate?.setText(movie.releaseDate)

        }
    }
}