package com.example.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.GenreResponse
import com.example.movieapp.model.MovieList
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.model.CreditDetails
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieDetails
import com.example.movieapp.model.SearchResponse
import com.example.movieapp.model.VideoDetails
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _popularMoviesLiveData = MutableLiveData<MovieList>()
    val popularMoviesLiveData: LiveData<MovieList> get() = _popularMoviesLiveData

    private val _genreList = MutableLiveData<GenreResponse>()
    val genreList: LiveData<GenreResponse> get() = _genreList

    var currentMovieId = MutableLiveData<Int>()

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> get() = _movieDetails

    private val _creditDetails = MutableLiveData<CreditDetails>()
    val creditDetails: LiveData<CreditDetails> get() = _creditDetails

    private val _videoDetails = MutableLiveData<VideoDetails>()
    val videoDetails: LiveData<VideoDetails> get() = _videoDetails

    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    private val _topRatedMoviesLiveData = MutableLiveData<MovieList>()
    val topRatedMoviesLiveData: LiveData<MovieList> get() = _topRatedMoviesLiveData

    private val _nowPlayingMoviesLiveData = MutableLiveData<MovieList>()
    val nowPlayingMoviesLiveData: LiveData<MovieList> get() = _nowPlayingMoviesLiveData

    private val _upcomingMoviesLiveData = MutableLiveData<MovieList>()
    val upcomingMoviesLiveData: LiveData<MovieList> get() = _upcomingMoviesLiveData

    private val _trendingMoviesLiveData = MutableLiveData<MovieList>()
    val trendingMoviesLiveData: LiveData<MovieList> get() = _trendingMoviesLiveData

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

    suspend fun getVideoDetails(movieId: Int, apiKey: String){
        val response = repository.getVideoDetails(movieId, apiKey)
        if(response.isSuccessful){
            response.body()?.let{
                _videoDetails.postValue(it)
                Log.i("Retrofit", "Video details = $it")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getSearchResults(query: String, apiKey: String){
        val response = repository.searchMovie(query, apiKey)
        if(response.isSuccessful){
            response.body()?.let{
                _searchResult.postValue(it)
                Log.i("Retrofit", "Search results = $it")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getTopRatedMovies(apiKey: String){
        val response = repository.getTopRatedMovies(apiKey)
        if(response.isSuccessful){
            response.body()?.let {
                _topRatedMoviesLiveData.postValue(it)
                Log.i("Retrofit", "Top rated movies = ${it.results}")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getNowPlayingMovies(apiKey: String){
        val response = repository.getNowPlayingMovies(apiKey)
        if(response.isSuccessful){
            response.body()?.let {
                _nowPlayingMoviesLiveData.postValue(it)
                Log.i("Retrofit", "Now playing movies = ${it.results}")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getUpcomingMovies(apiKey: String){
        val response = repository.getUpcomingMovies(apiKey)
        if(response.isSuccessful){
            response.body()?.let {
                _upcomingMoviesLiveData.postValue(it)
                Log.i("Retrofit", "Upcoming movies = ${it.results}")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }

    suspend fun getTrendingMovies(timeWindow: String, apiKey: String){
        val response = repository.getTrendingMovies(timeWindow, apiKey)
        if(response.isSuccessful){
            response.body()?.let {
                _trendingMoviesLiveData.postValue(it)
                Log.i("Retrofit", "Trending movies = ${it.results}")
            }
        } else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }
}