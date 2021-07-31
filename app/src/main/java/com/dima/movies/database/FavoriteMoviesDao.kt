package com.dima.movies.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dima.movies.model.Movie

@Dao
interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(movie: Movie)

    @Delete
    fun delete(movie: Movie)
}