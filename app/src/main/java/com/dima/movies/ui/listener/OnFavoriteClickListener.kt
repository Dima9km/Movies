package com.dima.movies.ui.listener

import com.dima.movies.model.Movie

interface OnFavoriteClickListener {
    fun onClickFavorite(movie: Movie)
}