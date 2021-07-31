package com.dima.movies.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dima.movies.MainRepository
import com.dima.movies.model.AllMoviesResponse
import com.dima.movies.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository) : ViewModel() {

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
}