package com.example.movieapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.Movie
import com.example.movieapp.utils.APIConstants

class TrendingMoviesRVAdapter(
    var movies: List<Movie>,
    private val trendingMoviesClickListener: TrendingMoviesClickListener):
    RecyclerView.Adapter<TrendingMoviesRVAdapter.TrendingMoviesViewHolder>() {

    inner class TrendingMoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val trendingMoviePosterImageView: ImageView = itemView.findViewById(R.id.trendingMoviePosterImageView)
        val trendingMovieNumberTextView: TextView = itemView.findViewById(R.id.trendingMovieNumberTextView)
        val trendingMoviesListLayout: RelativeLayout = itemView.findViewById(R.id.trendingMoviesListLayout)
    }

    interface TrendingMoviesClickListener{
        fun movieOnClickListener(movieId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingMoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trending_list, parent, false)
        return TrendingMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun onBindViewHolder(holder: TrendingMoviesViewHolder, position: Int) {
        val actualPosition = position % movies.size
        if(!movies.isNullOrEmpty()){
            val imageUrl = APIConstants.IMAGE_PATH + movies[actualPosition].poster_path
            Glide.with(holder.trendingMoviePosterImageView)
                .load(imageUrl)
                .into(holder.trendingMoviePosterImageView)
            holder.trendingMovieNumberTextView.text = (actualPosition + 1).toString()
            holder.trendingMoviesListLayout.setOnClickListener {
                trendingMoviesClickListener.movieOnClickListener(movies[actualPosition].id!!)
            }
        } else {
            Toast.makeText(holder.itemView.context, "No movies found", Toast.LENGTH_SHORT).show()
        }
    }
}