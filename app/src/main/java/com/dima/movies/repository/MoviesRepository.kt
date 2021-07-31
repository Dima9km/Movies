package com.dima.movies.repository

import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.network.MoviesApi
import retrofit2.Call

class MoviesRepository(private val moviesApi: MoviesApi) {

    fun getAllMovies(): Call<AllMoviesResponse> = moviesApi.getAllMovies(
        MoviesApi.APIKEY, MoviesApi.LANG
    )

    fun searchMovies(userQuery: String): Call<AllMoviesResponse> = moviesApi.searchMovies(
        MoviesApi.APIKEY, MoviesApi.LANG, userQuery
    )

    fun saveFavorites() {
    }

    fun loadFavorites() {
    }

}