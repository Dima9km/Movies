package com.dima.movies.ui

import android.os.Bundle
import android.view.View
import android.widget.*
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
import com.dima.movies.ui.listener.OnFavoriteClickListener
import com.dima.movies.viewmodel.MainViewModel
import com.dima.movies.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private var etSearch: EditText? = null
    private var llNotFound: LinearLayout? = null
    private var tvNotFound: TextView? = null
    private var llError: LinearLayout? = null
    private var pbRoundProgress: ProgressBar? = null
    private var pbLinearProgress: ProgressBar? = null
    private var fabRefresh: FloatingActionButton? = null

    private val viewModel: MainViewModel by lazy {
        val moviesApi = MoviesApi.getInstance()
        val moviesDb = MoviesDb.getInstance(this)

        ViewModelProvider(
            this,
            MainViewModelFactory(MoviesRepository(moviesApi, moviesDb))
        ).get(
            MainViewModel::class.java
        )
    }

    private val adapter = MainAdapter(onFavoriteClickListener = object : OnFavoriteClickListener {
        override fun onClickFavorite(movie: Movie) {
            viewModel.updateMovie(movie)
        }
    })

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

        viewModel.emptyResponse.observe(this, { isEmpty ->
            if (isEmpty == true) {
                llNotFound?.visibility = View.VISIBLE
            } else {
                llNotFound?.visibility = View.GONE
            }
        })

        viewModel.emptyResponseText.observe(this, { text ->
            tvNotFound?.text = text
        })
    }

    private fun initUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvMovies)
        recyclerView.adapter = adapter

        llNotFound = findViewById(R.id.llNotFound)
        tvNotFound = findViewById(R.id.tvNotFound)
        llError = findViewById(R.id.llError)
        pbRoundProgress = findViewById(R.id.pbRoundProgress)
        pbLinearProgress = findViewById(R.id.pbLinearProgress)
        fabRefresh = findViewById(R.id.fabRefresh)

        etSearch = findViewById(R.id.etSearch)
        etSearch?.doAfterTextChanged { text ->
            viewModel.searchMovies(userQuery = text?.toString() ?: "")
        }

        val srlRefresh = findViewById<SwipeRefreshLayout>(R.id.srlRefresh)
        srlRefresh.setOnRefreshListener { viewModel.getAllMovies() }
    }
}