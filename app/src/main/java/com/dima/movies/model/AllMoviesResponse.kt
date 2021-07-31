package com.dima.movies.model

data class AllMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Long
) {
}