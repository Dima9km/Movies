package com.dima.movies.repository

import com.dima.movies.database.MoviesDb
import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.model.Movie
import com.dima.movies.network.MoviesApi
import retrofit2.Call

class MoviesRepository(
    private val moviesApi: MoviesApi,
    private val moviesDb: MoviesDb
) {

    fun getAllMovies(): Call<AllMoviesResponse> =
        moviesApi.getAllMovies(
        apiKey = MoviesApi.APIKEY,
        language = MoviesApi.LANG
    )

    fun searchMovies(userQuery: String): Call<AllMoviesResponse> =
        moviesApi.searchMovies(
            apiKey = MoviesApi.APIKEY,
            language = MoviesApi.LANG,
            query = userQuery
    )

    fun saveMovies(movie: Movie) {
        moviesDb.favoriteMoviesDao().add(movie)
    }

    fun deleteMovies(movie: Movie) {
        moviesDb.favoriteMoviesDao().delete(movie)
    }

    fun getMoviesFromDB(): List<Movie> {
        return moviesDb.favoriteMoviesDao().getAll()
    }

    fun getMovieById(id: Long): Movie {
        return moviesDb.favoriteMoviesDao().getMovieById(id)
    }

}