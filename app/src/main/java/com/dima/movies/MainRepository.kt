package com.dima.movies

import com.dima.movies.network.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllMovies() = retrofitService.getAllMovies()

    fun searchMovies() = retrofitService.searchMovies()

    fun saveFavorites() {

    }

    fun loadFavorites() {

    }

}