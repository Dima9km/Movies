package com.dima.movies.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dima.movies.R
import com.dima.movies.database.MoviesDb
import com.dima.movies.model.Movie
import com.dima.movies.network.MoviesApi
import com.dima.movies.repository.MoviesRepository
import com.dima.movies.viewmodel.MainViewModel
import com.dima.movies.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), OnFavoriteClickedListener {

    private lateinit var etSearch: EditText

    private val viewModel: MainViewModel by lazy {
        val moviesApi = MoviesApi.getInstance()
        val database = MoviesDb.getInstance(this)

        ViewModelProvider(
            this,
            MainViewModelFactory(MoviesRepository(moviesApi, database))
        ).get(
            MainViewModel::class.java
        )
    }

    private val adapter = MainAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllMovies()
    }

    private fun initLiveData() {
        viewModel.moviesList.observe(this, { movies ->
            adapter.movies = movies.toMutableList()
        })

        viewModel.errorMessage.observe(this, { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
    }

    private fun initUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvMovies)
        recyclerView.adapter = adapter

        val errorAlert = findViewById<LinearLayout>(R.id.llError)
        val notFoundAlert = findViewById<LinearLayout>(R.id.llNotFound)
        val roundProgress = findViewById<ProgressBar>(R.id.pbRoundProgress)
        val linearProgress = findViewById<ProgressBar>(R.id.pbLinearProgress)
        val refresh = findViewById<FloatingActionButton>(R.id.fabRefresh)

        etSearch = findViewById(R.id.etSearch)
        etSearch.doAfterTextChanged { text -> viewModel.searchMovies(text.toString()) }

        val srlRefresh = findViewById<SwipeRefreshLayout>(R.id.srlRefresh)
        srlRefresh.setOnRefreshListener { viewModel.getAllMovies() }
    }

    override fun clickedFavorite(movie: Movie) {
        viewModel.organizeFavorite(movie)
    }

}