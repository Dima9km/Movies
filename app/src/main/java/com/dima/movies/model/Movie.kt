package com.dima.movies.model

import android.arch.persistence.room.PrimaryKey

data class Movie(
    @PrimaryKey
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    val isFavorite: Boolean
) {
}