package com.dima.movies.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Movie(
    @PrimaryKey
    val id: Long,
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    var isFavorite: Boolean
) {
    val formattedCreatedAt: String
        get() {
            val inputDate =
                SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(releaseDate)
            return SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
                .format(inputDate)
        }
}