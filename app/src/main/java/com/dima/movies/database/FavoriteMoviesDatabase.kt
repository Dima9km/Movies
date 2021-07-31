package com.dima.movies.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.dima.movies.model.Movie

@Database(entities = arrayOf(Movie::class), version = 1)
abstract class FavoriteMoviesDatabase : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao
}