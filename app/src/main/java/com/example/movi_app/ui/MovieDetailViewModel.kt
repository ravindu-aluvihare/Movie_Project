package com.example.movi_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movi_app.data.MovieRepository
import com.example.movi_app.model.Movie
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = repository.getMovieDetails(movieId)
                _movie.postValue(movieDetails)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}