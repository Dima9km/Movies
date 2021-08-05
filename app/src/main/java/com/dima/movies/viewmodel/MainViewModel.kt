package com.dima.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.model.Movie
import com.dima.movies.repository.MoviesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _events = MutableLiveData<MainEvent>()
    val events: LiveData<MainEvent> get() = _events

    fun getAllMovies() {
        val response = repository.getAllMovies()
        _events.value = MainEvent.ErrorResponse(false)
        _events.value = MainEvent.RoundProgressShown(true)
        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                _events.value = MainEvent.ErrorResponse(false)
                if (response.body() != null) {
                    _events.value = MainEvent.MoviesList(formatMovies(response.body()!!.results))
                }
                if (response.body()?.results?.size == 0) {
                    _events.value = MainEvent.EmptyResponse(true)
                } else {
                    _events.value = MainEvent.EmptyResponse(false)
                }
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                _events.value = MainEvent.EmptyResponse(false)
                _events.value = MainEvent.ErrorMessage(t.message)
                _events.value = MainEvent.ErrorResponse(true)
            }

        })
        _events.value = MainEvent.RoundProgressShown(false)
    }

    fun searchMovies(userQuery: String) {
        val response = repository.searchMovies(userQuery)
        _events.value = MainEvent.RoundProgressShown(true)

        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                _events.value = MainEvent.ErrorResponse(false)
                if (response.body() != null) {
                    _events.value = MainEvent.MoviesList(formatMovies(response.body()!!.results))

                }
                if (response.body()?.results?.isEmpty() == true) {
                    _events.value = MainEvent.EmptyResponse(true)
                    _events.value = MainEvent.EmptyResponseText(
                        String.format(
                            "По вашему запросу «%s»\nничего не найдено",
                            userQuery
                        )
                    )
                } else {
                    _events.value = MainEvent.EmptyResponse(false)
                }
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                _events.value = MainEvent.EmptyResponse(false)
                _events.value = MainEvent.ErrorMessage(t.message)
                _events.value = MainEvent.ErrorResponse(true)
            }
        })
        _events.value = MainEvent.RoundProgressShown(false)
    }

    fun updateMovie(movie: Movie) {
        if (!movie.isFavorite) {
            repository.deleteMovie(movie)
        } else {
            repository.saveMovie(movie)
        }
    }

    private fun formatMovies(movies: List<Movie>): List<Movie> {
        val moviesDb = repository.getMoviesFromDB()
        movies.forEach { movie ->
            movie.isFavorite = moviesDb.any { movieDb -> movieDb.id == movie.id }
        }
        return movies
    }

    sealed class MainEvent {
        data class MoviesList(val movies: List<Movie>) : MainEvent()
        data class ErrorMessage(val text: String?) : MainEvent()
        data class EmptyResponse(val emptyResponse: Boolean) : MainEvent()
        data class EmptyResponseText(val emptyResponseText: String) : MainEvent()
        data class ErrorResponse(val errorResponse: Boolean) : MainEvent()
        data class RoundProgressShown(val roundProgressShown: Boolean) : MainEvent()
        data class LinearProgressShown(val linearProgressShown: Boolean) : MainEvent()
    }
}