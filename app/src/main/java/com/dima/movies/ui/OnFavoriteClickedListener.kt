package com.dima.movies.ui

import com.dima.movies.model.Movie

interface OnFavoriteClickedListener {
    fun clickedFavorite(movie: Movie)
}