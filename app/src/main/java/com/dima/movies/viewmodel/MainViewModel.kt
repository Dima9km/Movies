package com.dima.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.model.Movie
import com.dima.movies.repository.MoviesRepository
import com.dima.movies.viewmodel.MainViewModel.MainEvent.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _events = MutableLiveData<MainEvent>()
    val events: LiveData<MainEvent> get() = _events

    fun getAllMovies() {
        _events.value = ProgressVisible(isShown = true)
        val response = repository.getAllMovies()
        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                when {
                    response.body() != null -> {
                        _events.value = NotEmpty(formatMovies(response.body()!!.results))
                        _events.value = ProgressVisible(isShown = false)
                    }
                    else -> {
                        _events.value = ErrorResponse(isShown = false)
                        _events.value = ProgressVisible(isShown = false)
                    }
                }
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                _events.value = ErrorResponse(isShown = true)
                _events.value = NetworkError(isShown = true)
                _events.value = Empty(isShown = false, query = null)
                _events.value = ProgressVisible(isShown = false)
            }
        })
        _events.value = RefreshEnded(refreshEnded = true)
    }

    fun searchMovies(userQuery: String) {
        _events.value = ProgressVisible(isShown = true)
        val response = repository.searchMovies(userQuery)
        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                when {
                    response.body()?.results?.isEmpty() == false -> {
                        _events.value = NotEmpty(formatMovies(response.body()!!.results))
                        _events.value = Empty(isShown = false, query = "")
                        _events.value = ProgressVisible(isShown = false)
                    }
                    response.body()?.results?.isEmpty() == true -> {
                        _events.value = NotEmpty(formatMovies(response.body()!!.results))
                        _events.value = Empty(
                            isShown = true,
                            query = "По вашему запросу «$userQuery»\nничего не найдено"
                        )
                        _events.value = ProgressVisible(isShown = false)

                    }
                    else -> {
                        _events.value = Empty(isShown = false, query = "")
                        _events.value = ProgressVisible(isShown = false)
                    }
                }
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                _events.value = NetworkError(isShown = true)
                _events.value = ErrorResponse(isShown = true)
                _events.value = Empty(isShown = false, query = null)
                _events.value = ProgressVisible(isShown = false)
            }
        })
        _events.value = RefreshEnded(refreshEnded = true)
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
        data class NotEmpty(val movies: List<Movie>) : MainEvent()
        data class Empty(val isShown: Boolean, val query: String?) : MainEvent()
        data class ErrorResponse(val isShown: Boolean) : MainEvent()
        data class ProgressVisible(val isShown: Boolean) : MainEvent()
        data class RefreshEnded(val refreshEnded: Boolean) : MainEvent()
        data class NetworkError(val isShown: Boolean) : MainEvent()
    }
}
