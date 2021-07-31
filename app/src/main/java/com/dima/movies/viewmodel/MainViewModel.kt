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

    fun getAllMovies() {
        val response = repository.getAllMovies()
        response.enqueue(object : Callback<AllMoviesResponse> {
            override fun onResponse(
                call: Call<AllMoviesResponse>,
                response: Response<AllMoviesResponse>
            ) {
                moviesList.postValue(response.body()?.results)
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
                moviesList.postValue(response.body()?.results)
            }

            override fun onFailure(call: Call<AllMoviesResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}