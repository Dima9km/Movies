package com.dima.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dima.movies.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class FavoriteMoviesDatabase : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao
}