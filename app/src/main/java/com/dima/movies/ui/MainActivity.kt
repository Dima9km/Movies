package com.dima.movies.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dima.movies.MainRepository
import com.dima.movies.R
import com.dima.movies.network.RetrofitService
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
                MainViewModel::class.java
            )
        viewModel.movieList.observe(this, {
            adapter.setMovieList(it)
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG ).show()
        })
        viewModel.getAllMovies()
    }

    private fun initUI() {
        val recyclerView: RecyclerView = findViewById(R.id.rvMovies)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val errorAlert: LinearLayout = findViewById(R.id.llError)
        errorAlert.visibility = View.GONE
        val notFoundAlert: LinearLayout = findViewById(R.id.llNotFound)
        notFoundAlert.visibility = View.GONE
    }

}