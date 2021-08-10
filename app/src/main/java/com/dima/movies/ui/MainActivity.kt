package com.dima.movies.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dima.movies.R
import com.dima.movies.database.MoviesDb
import com.dima.movies.model.Movie
import com.dima.movies.network.MoviesApi
import com.dima.movies.repository.MoviesRepository
import com.dima.movies.ui.listener.OnClickFavoriteListener
import com.dima.movies.viewmodel.MainViewModel
import com.dima.movies.viewmodel.MainViewModel.MainEvent.*
import com.dima.movies.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : FragmentActivity() {

    private var etSearch: EditText? = null
    private var llNotFound: LinearLayout? = null
    private var tvNotFound: TextView? = null
    private var llError: LinearLayout? = null
    private var pbRoundProgress: ProgressBar? = null
    private var pbLinearProgress: ProgressBar? = null
    private var fabRefresh: FloatingActionButton? = null
    private var srlRefresh: SwipeRefreshLayout? = null
    private var rvMovies: RecyclerView? = null


    private val viewModel: MainViewModel by lazy {
        val moviesApi = MoviesApi.getInstance()
        val moviesDb = MoviesDb.getInstance(context = this)

        ViewModelProvider(this, MainViewModelFactory(MoviesRepository(moviesApi, moviesDb)))
            .get(MainViewModel::class.java)
    }

    private val adapter = MainAdapter(onClickFavoriteListener = object : OnClickFavoriteListener {
        override fun onClickFavorite(movie: Movie) {
            viewModel.updateMovie(movie)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initLiveData()
        viewModel.getAllMovies()
    }

    private fun initLiveData() {

        viewModel.events.observe(this, { event ->
            when (event) {
                is NotEmpty ->
                    adapter.movies = event.movies.toMutableList()

                is NetworkError ->
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Проверьте соединение с интернетом и попробуйте ещё раз",
                        Snackbar.LENGTH_LONG
                    ).show()

                is Empty ->
                    when {
                        event.isShown -> {
                            llNotFound?.visibility = View.VISIBLE
                            tvNotFound?.text = event.query
                        }
                        else -> {
                            llNotFound?.visibility = View.GONE
                        }
                    }

                is ErrorResponse ->
                    when {
                        event.isShown -> {
                            llError?.visibility = View.VISIBLE
                            fabRefresh?.visibility = View.VISIBLE
                            rvMovies?.visibility = View.GONE
                        }
                        else -> {
                            llError?.visibility = View.GONE
                            fabRefresh?.visibility = View.GONE
                            rvMovies?.visibility = View.VISIBLE
                        }
                    }

                is ProgressVisible ->
                    when {
                        event.isShown.not() -> {
                            pbRoundProgress?.visibility = View.GONE
                            pbLinearProgress?.visibility = View.GONE
                        }
                        adapter.movies.isEmpty() -> {
                            pbRoundProgress?.visibility = View.VISIBLE
                            pbLinearProgress?.visibility = View.GONE
                        }
                        adapter.movies.isNotEmpty() -> {
                            pbRoundProgress?.visibility = View.GONE
                            pbLinearProgress?.visibility = View.VISIBLE
                        }
                    }

                is RefreshEnded ->
                    srlRefresh?.isRefreshing = false
            }
        })
    }

    private fun initUI() {
        llNotFound = findViewById(R.id.llNotFound)
        tvNotFound = findViewById(R.id.tvNotFound)
        llError = findViewById(R.id.llError)
        pbRoundProgress = findViewById(R.id.pbRoundProgress)
        pbLinearProgress = findViewById(R.id.pbLinearProgress)

        fabRefresh = findViewById(R.id.fabRefresh)
        fabRefresh?.setOnClickListener {
            viewModel.getAllMovies()
            etSearch?.text = null
        }

        rvMovies = findViewById(R.id.rvMovies)
        rvMovies?.requestFocus()
        rvMovies?.adapter = adapter

        etSearch = findViewById(R.id.etSearch)
        etSearch?.doAfterTextChanged { text ->
            viewModel.searchMovies(userQuery = text?.toString() ?: "")
        }

        srlRefresh = findViewById(R.id.srlRefresh)
        srlRefresh?.setOnRefreshListener {
            viewModel.getAllMovies()
            etSearch?.text = null
        }
    }
}