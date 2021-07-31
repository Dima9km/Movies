package com.dima.movies.viewmodel.repository

import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.network.RetrofitService
import retrofit2.Call

class MainRepository(private val retrofitService: RetrofitService) {

    fun getAllMovies(): Call<AllMoviesResponse> = retrofitService.getAllMovies(
        RetrofitService.APIKEY, RetrofitService.LANG
    )

    fun searchMovies(userQuery: String): Call<AllMoviesResponse> = retrofitService.searchMovies(
        RetrofitService.APIKEY, RetrofitService.LANG, userQuery
    )

    fun saveFavorites() {
    }

    fun loadFavorites() {
    }

}