package com.dima.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.model.Movie
import com.dima.movies.repository.MoviesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MoviesRepository) : ViewModel() {

    val moviesList = MutableLiveData<List<Movie>>()
    val errorMessage = MutableLiveData<String>()
    val emptyResponse = MutableLiveData<Boolean>()
    val emptyResponseText = MutableLiveData<String>()

    init {
        emptyResponse.postValue(false)
    }

    fun getAllMovies() {
        val response = repository.getAllMovies()

        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                moviesList.postValue(response.body()?.results)
                if (response.body()?.results?.size == 0) {
                    emptyResponse.postValue(true)
                } else {
                    emptyResponse.postValue(false)
                }
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                errorMessage.postValue(t.message)

            }
        })
    }

    fun searchMovies(userQuery: String) {
        val response = repository.searchMovies(userQuery)
        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                if (response.body() != null) {
                    moviesList.postValue(formatMovies(response.body()!!.results))
                }
                if (response.body()?.results?.isEmpty() == true) {
                    emptyResponse.postValue(true)
                    emptyResponseText.postValue(String
                        .format("По вашему запросу «%s» ничего не найдено", userQuery))
                } else {
                    emptyResponse.postValue(false)
                }
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun updateMovie(movie: Movie) {
        if (!movie.isFavorite) {
            repository.deleteMovie(movie)
        } else {
            repository.saveMovie(movie)
        }
    }

    private fun formatMovies(movies: List<Movie>): List<Movie> {
        movies.forEach { movie ->
            movie.isFavorite = movie.id == repository.getMovieById(movie.id)?.id
        }
        return movies
    }
}