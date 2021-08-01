package com.dima.movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dima.movies.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MoviesDb : RoomDatabase() {
    abstract fun favoriteMoviesDao(): MoviesDao


    companion object {
        fun getInstance(context: Context): MoviesDb {
            return Room.databaseBuilder(context, MoviesDb::class.java, "favorites")
                .allowMainThreadQueries()
                .build()
        }
    }
}