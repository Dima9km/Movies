package com.dima.movies.viewmodel.repository

import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.model.Movie
import com.dima.movies.network.RetrofitService
import retrofit2.Call

class MainRepository constructor(private val retrofitService: RetrofitService, ) {

    constructor (retrofitService: RetrofitService, _userQuery: String) : this(retrofitService) {
        userQuery = userQuery
    }

    var userQuery: String = ""

    fun getAllMovies(): Call<AllMoviesResponse> = retrofitService.getAllMovies(
        "6ccd72a2a8fc239b13f209408fc31c33", "ru"
    )

    fun searchMovies() = retrofitService.searchMovies(
        "6ccd72a2a8fc239b13f209408fc31c33", "ru", userQuery
    )

    fun saveFavorites() {

    }

    fun loadFavorites() {

    }

}