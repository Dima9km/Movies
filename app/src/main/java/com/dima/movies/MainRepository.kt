package com.dima.movies

import com.dima.movies.network.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllMovies() = retrofitService.getAllMovies("6ccd72a2a8fc239b13f209408fc31c33", "ru")

    fun searchMovies() = retrofitService.searchMovies("6ccd72a2a8fc239b13f209408fc31c33", "ru")

    fun saveFavorites() {

    }

    fun loadFavorites() {

    }

}