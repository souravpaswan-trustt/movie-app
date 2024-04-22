package com.example.movieapp.ui.favouritesScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.R
import com.example.movieapp.database.FavouriteMovieDb
import com.example.movieapp.database.FavouriteMovieRepository
import com.example.movieapp.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouriteMovieViewModel: FavouriteMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        val dao = FavouriteMovieDb.getInstance(requireActivity()).favouriteMovieDao
        val favRepository = FavouriteMovieRepository(dao)
        val factory = FavouriteMovieViewModelFactory(favRepository)
        favouriteMovieViewModel = ViewModelProvider(requireActivity(), factory)[FavouriteMovieViewModel::class.java]
        favouriteMovieViewModel.favourites.observe(viewLifecycleOwner, Observer {
            Log.i("RoomDB", it.toString())
        })
        return binding.root
    }
}