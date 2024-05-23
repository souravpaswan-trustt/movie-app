package com.example.movieapp.ui.favouritesScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.database.FavouriteMovie
import com.example.movieapp.database.FavouriteMovieRepository
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteMovieViewModel(private val repository: FavouriteMovieRepository): ViewModel() {

    val favourites = repository.favouriteMovies

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    private val _currentMovie = MutableLiveData<FavouriteMovie?>()
    val currentMovie: LiveData<FavouriteMovie?> get() = _currentMovie

    fun insert(favouriteMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.insert(favouriteMovie)
            getCurrentMovie(favouriteMovie)
        } catch (e: Exception) {
            Log.e("RoomDB", "Failed to insert movie", e)
            _statusMessage.postValue("Something went wrong!")
        }
    }

    fun remove(favouriteMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(favouriteMovie)
            getCurrentMovie(favouriteMovie)
        } catch (e: Exception) {
            _statusMessage.postValue("Something went wrong!")
        }
    }

    fun getCurrentMovie(favouriteMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val movie = repository.getMovieById(favouriteMovie.movieId)
            _currentMovie.postValue(movie)
        } catch (e: Exception) {
            _statusMessage.postValue("Something went wrong!")
        }
    }
}