package com.dima.movies

import android.app.Application
import android.arch.persistence.room.Room
import com.dima.movies.database.FavoriteMoviesDatabase

class App : Application() {

    lateinit var favoriteMoviesDatabase: FavoriteMoviesDatabase

    override fun onCreate() {
        super.onCreate()
            favoriteMoviesDatabase = Room.databaseBuilder(this, FavoriteMoviesDatabase::class.java, "favorites")
                .allowMainThreadQueries()
                .build()
    }
}