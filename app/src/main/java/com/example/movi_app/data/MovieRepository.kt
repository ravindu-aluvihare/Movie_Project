package com.example.movi_app.data

import com.example.movi_app.model.Movie
import com.example.movi_app.model.MovieResponse
import com.example.movi_app.utils.Constants

class MovieRepository(private val apiService: MovieApiService) {
    suspend fun getPopularMovies(page: Int = 1): List<Movie> {
        return apiService.getPopularMovies(Constants.TMDB_API_KEY, page).movies
    }

    suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.getMovieDetails(movieId, Constants.TMDB_API_KEY)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        val response =  apiService.searchMovies(query, Constants.TMDB_API_KEY)
        return response.movies
    }

}