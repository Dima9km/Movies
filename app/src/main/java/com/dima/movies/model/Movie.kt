package com.dima.movies.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    val id: Long,
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    var isFavorite: Boolean
)