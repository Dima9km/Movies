package com.dima.movies.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dima.movies.R
import com.dima.movies.network.RetrofitService
import com.dima.movies.viewmodel.MainViewModel
import com.dima.movies.viewmodel.MainViewModelFactory
import com.dima.movies.viewmodel.repository.MainRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText

    private val viewModel: MainViewModel by lazy {
        val retrofitService = RetrofitService.getInstance()

        ViewModelProvider(this, MainViewModelFactory(MainRepository(retrofitService))).get(
            MainViewModel::class.java
        )
    }

    private val adapter = MainAdapter()

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
            adapter.setMoviesList(movies)
        })

        viewModel.errorMessage.observe(this, { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
    }

    private fun initUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvMovies)
        recyclerView.adapter = adapter

        val errorAlert = findViewById<LinearLayout>(R.id.llError)
        errorAlert.visibility = View.GONE
        val notFoundAlert = findViewById<LinearLayout>(R.id.llNotFound)
        notFoundAlert.visibility = View.GONE
        val preloader = findViewById<ProgressBar>(R.id.pbRoundProgress)
        preloader.visibility = View.GONE
        val progress = findViewById<ProgressBar>(R.id.pbLinearProgress)
        progress.visibility = View.GONE
        val fabRefresh = findViewById<FloatingActionButton>(R.id.fabRefresh)
        fabRefresh.visibility = View.GONE

        etSearch = findViewById(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                viewModel.searchMovies(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        val srlRefresh = findViewById<SwipeRefreshLayout>(R.id.srlRefresh)
        srlRefresh.setOnRefreshListener {
            viewModel.getAllMovies()
            srlRefresh.visibility = View.GONE
        }
    }

}