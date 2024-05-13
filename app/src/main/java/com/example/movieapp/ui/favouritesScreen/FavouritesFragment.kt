package com.example.movieapp.ui.favouritesScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.database.FavouriteMovie
import com.example.movieapp.database.FavouriteMovieDb
import com.example.movieapp.database.FavouriteMovieRepository
import com.example.movieapp.databinding.FragmentFavouritesBinding
import com.example.movieapp.viewmodel.MainViewModel
import com.example.movieapp.viewmodel.MainViewModelFactory

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favouriteMovieViewModel: FavouriteMovieViewModel
    private lateinit var favouriteMoviesAdapter: FavouriteMoviesRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)

        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        val dao = FavouriteMovieDb.getInstance(requireActivity()).favouriteMovieDao
        val favRepository = FavouriteMovieRepository(dao)
        val factory = FavouriteMovieViewModelFactory(favRepository)
        favouriteMovieViewModel = ViewModelProvider(requireActivity(), factory)[FavouriteMovieViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouriteMoviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        favouriteMoviesAdapter = FavouriteMoviesRVAdapter(
            emptyList(),
            object : FavouriteMoviesRVAdapter.FavouriteMoviesRVAdapterClickListener{
                override fun showFavouriteMovieDetailsOnClick(movieId: Int) {
                    mainViewModel.currentMovieId.value = movieId
                    findNavController().navigate(R.id.action_navigation_favourites_to_movieDetailsFragment2)
                }

                override fun removeFavouriteOnClickListener(id: Int, name: String, release: String, url: String, rating: Double) {
                    val favouriteMovie = FavouriteMovie(id, name, release, url, rating)
                    favouriteMovieViewModel.remove(favouriteMovie)
                    favouriteMovieViewModel.statusMessage.observe(viewLifecycleOwner, Observer { message->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    })
                }
            }
        )
        binding.favouriteMoviesRecyclerView.adapter = favouriteMoviesAdapter

        favouriteMovieViewModel.favourites.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                favouriteMoviesAdapter.favourites = it
                favouriteMoviesAdapter.notifyDataSetChanged()
            } else{
                Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
            }
        })
    }
}