package com.dima.movies.network

import com.dima.movies.BuildConfig
import com.dima.movies.model.AllMoviesResponse
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MoviesApi {

    @GET("discover/movie")
    fun getAllMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<AllMoviesResponse>

    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String
    ): Call<AllMoviesResponse>

    companion object {
        private var moviesApi: MoviesApi? = null
        private const val BASEURL = "https://api.themoviedb.org/3/"
        const val APIKEY = "6ccd72a2a8fc239b13f209408fc31c33"
        const val LANG = "ru"

        fun getInstance(): MoviesApi {
            if (moviesApi == null) {
                val gson = GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

                val okHttpInterceptorLogging = LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BODY)
                    .log(Platform.INFO)
                    .tag("REST")
                    .build()

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(okHttpInterceptorLogging)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .callTimeout(60, TimeUnit.SECONDS)
                    .build()

                moviesApi = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BASEURL)
                    .client(okHttpClient)
                    .build()
                    .create(MoviesApi::class.java)
            }
            return moviesApi!!
        }
    }
}