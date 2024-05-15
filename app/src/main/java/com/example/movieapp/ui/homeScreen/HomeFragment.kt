package com.example.movieapp.ui.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.database.FavouriteMovieDb
import com.example.movieapp.database.FavouriteMovieRepository
import com.example.movieapp.ui.favouritesScreen.FavouriteMovieViewModel
import com.example.movieapp.ui.favouritesScreen.FavouriteMovieViewModelFactory
import com.example.movieapp.utils.APIConstants
import com.example.movieapp.viewmodel.MainViewModel
import com.example.movieapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favouriteMovieViewModel: FavouriteMovieViewModel
    private lateinit var favRepository: FavouriteMovieRepository
    private lateinit var popularMoviesAdapter: MoviesListRVAdapter
    private lateinit var topRatedMoviesAdapter: MoviesListRVAdapter
    private lateinit var nowPlayingMoviesAdapter: MoviesListRVAdapter
    private lateinit var upcomingMoviesAdapter: MoviesListRVAdapter
    private lateinit var trendingMoviesAdapter: MoviesListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        val dao = FavouriteMovieDb.getInstance(requireActivity()).favouriteMovieDao
        favRepository = FavouriteMovieRepository(dao)
        val factory = FavouriteMovieViewModelFactory(favRepository)
        favouriteMovieViewModel = ViewModelProvider(requireActivity(), factory)[FavouriteMovieViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    activity?.finish()
                }
                .setNegativeButton("No") { _, _ ->
                    //do nothing
                }
                .show()
        }
        lifecycleScope.launch {
            mainViewModel.getGenres(APIConstants.API_KEY)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeRefreshLayout = binding.swipeRefreshLayout

        //initialising the adapters
        popularMoviesAdapter = initialiseAdapter(binding.popularRecyclerView)
        topRatedMoviesAdapter = initialiseAdapter(binding.topRatedRecyclerView)
        nowPlayingMoviesAdapter = initialiseAdapter(binding.nowPlayingRecyclerView)
        upcomingMoviesAdapter = initialiseAdapter(binding.upcomingRecyclerView)
        trendingMoviesAdapter = initialiseAdapter(binding.trendingThisWeekRecyclerView)

        swipeRefreshLayout.setOnRefreshListener {
            displayPopularMovies()
            displayTrendingMovies()
            displayTopRatedMovies()
            displayUpcomingMovies()
            displayNowPlayingMovies()
            swipeRefreshLayout.isRefreshing = false
        }
        displayPopularMovies()
        displayTrendingMovies()
        displayTopRatedMovies()
        displayUpcomingMovies()
        displayNowPlayingMovies()
    }

    fun initialiseAdapter(recyclerView: RecyclerView): MoviesListRVAdapter {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter =  MoviesListRVAdapter(
            emptyList(),
            object : MoviesListRVAdapter.MoviesRVAdapterClickListener {
                override fun movieOnClickListener(movieId: Int) {
                    mainViewModel.currentMovieId.value = movieId
                    findNavController().navigate(R.id.action_navigation_home_to_movieDetailsFragment2)
                }
            })
        recyclerView.adapter = adapter
        return adapter
    }

    fun displayPopularMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.getPopularMovies(APIConstants.API_KEY)
            withContext(Dispatchers.Main) {
                mainViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if (it != null && it.results.isNotEmpty()) {
                        popularMoviesAdapter.movies = it.results
                        popularMoviesAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun displayTopRatedMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.getTopRatedMovies(APIConstants.API_KEY)
            withContext(Dispatchers.Main) {
                mainViewModel.topRatedMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if (it != null && it.results.isNotEmpty()) {
                        topRatedMoviesAdapter.movies = it.results
                        topRatedMoviesAdapter.notifyDataSetChanged()
                        binding.topRatedRecyclerView.adapter = topRatedMoviesAdapter
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun displayNowPlayingMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.getNowPlayingMovies(APIConstants.API_KEY)
            withContext(Dispatchers.Main) {
                mainViewModel.nowPlayingMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if (it != null && it.results.isNotEmpty()) {
                        nowPlayingMoviesAdapter.movies = it.results
                        nowPlayingMoviesAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun displayUpcomingMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.getUpcomingMovies(APIConstants.API_KEY)
            withContext(Dispatchers.Main) {
                mainViewModel.upcomingMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if (it != null && it.results.isNotEmpty()) {
                        upcomingMoviesAdapter.movies = it.results
                        upcomingMoviesAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun displayTrendingMovies() {
        //("Not yet implemented")
    }
}