package com.example.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.GenreResponse
import com.example.movieapp.model.MovieList
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.model.CreditDetails
import com.example.movieapp.model.MovieDetails

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _popularMoviesLiveData = MutableLiveData<MovieList>()
    val popularMoviesLiveData: LiveData<MovieList> get() = _popularMoviesLiveData

    private val _genreList = MutableLiveData<GenreResponse>()
    val genreList: LiveData<GenreResponse> get() = _genreList

    var currentMovieId = MutableLiveData<Int>()

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> get() = _movieDetails

    private val _creditDetails = MutableLiveData<CreditDetails>()
    val creditDetails: LiveData<CreditDetails> get() = _creditDetails

    suspend fun getPopularMovies(apiKey: String) {
        val response = repository.getPopularMovies(apiKey)
        if (response.isSuccessful) {
            response.body()?.let {
                _popularMoviesLiveData.postValue(it)
                Log.i("Retrofit", "movie body = ${it.results}")
            }
        } else{
            Log.i("Retrofit", "Error: ${response.errorBody()}")
        }
    }

    suspend fun getGenres(apiKey: String){
        val response = repository.getGenres(apiKey)
        if(response.isSuccessful){
            response.body()?.let{
                _genreList.postValue(it)
                Log.i("Retrofit", "Genre body = $it")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getMovieDetails(movieId: Int, apiKey: String){
        Log.i("Retrofit","Movie id -> ${currentMovieId.value}")
        val response = repository.getMovieDetails(movieId, apiKey)
        if(response.isSuccessful){
            response.body()?.let {
                _movieDetails.postValue(it)
                Log.i("Retrofit", "Movie details body = $it")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getMovieCredits(movieId: Int, apiKey: String){
        val response = repository.getMovieCredits(movieId, apiKey)
        if(response.isSuccessful){
            response.body()?.let {
                _creditDetails.postValue(it)
                Log.i("Retrofit", "Credit details = $it")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }
}