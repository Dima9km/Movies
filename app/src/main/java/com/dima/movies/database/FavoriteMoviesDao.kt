package com.dima.movies.database

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import com.dima.movies.model.Movie

interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(movie: Movie?)

    @Delete
    fun deleteFavorite(movie: Movie?)
}