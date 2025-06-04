package com.example.movi_app.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movi_app.data.MovieRepository
import com.example.movi_app.model.Movie
import kotlinx.coroutines.launch

class MovieListViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> get() = _searchResults

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val movieList = repository.getPopularMovies()
                Log.d("MovieListViewModel", "Fetched movies: ${movieList.size}")
                _movies.postValue(movieList)
            } catch (e: Exception) {
                Log.e("MovieListViewModel", "Error fetching movies", e)
                _movies.postValue(emptyList())
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchMovies(query)
                Log.d("MovieListViewModel", "Search results for '$query': ${results.size}")
                _searchResults.postValue(results)
            } catch (e: Exception) {
                Log.e("MovieListViewModel", "Search error", e)
                _searchResults.postValue(emptyList())
            }
        }
    }


}