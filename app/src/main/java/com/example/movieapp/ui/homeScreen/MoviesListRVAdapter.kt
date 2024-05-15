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

class MoviesListRVAdapter(
    var movies: List<Movie>,
    private val moviesRVAdapterClickListener: MoviesRVAdapterClickListener
) :
    RecyclerView.Adapter<MoviesListRVAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val movieTitleTextView: TextView = itemView.findViewById(R.id.movieTitleTextView)
        val moviePosterImageView: ImageView = itemView.findViewById(R.id.moviePosterImageView)
        val movieReleaseDateTextView: TextView = itemView.findViewById(R.id.movieReleaseDateTextView)
        val movieRatingTextView: TextView = itemView.findViewById(R.id.movieRatingTextView)
//        var movieGenreTextView: TextView = null
        val movieDetailsLayout: RelativeLayout? = itemView.findViewById(R.id.movieDetailsLayout)

    }

    interface MoviesRVAdapterClickListener {
        fun movieOnClickListener(movieId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_list, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (!movies.isNullOrEmpty()) {
            val imageUrl = APIConstants.IMAGE_PATH + movies[position].poster_path
            holder.movieTitleTextView.text = movies[position].title
            holder.movieReleaseDateTextView.text = movies[position].release_date.substring(0, 4)
            holder.movieRatingTextView.text =
                movies[position].vote_average.toString().substring(0, 3)
            Glide.with(holder.moviePosterImageView)
                .load(imageUrl)
                .into(holder.moviePosterImageView)
            holder.movieDetailsLayout?.setOnClickListener {
                moviesRVAdapterClickListener.movieOnClickListener(movies[position].id)
            }
        } else {
            Toast.makeText(holder.itemView.context, "No movies found", Toast.LENGTH_SHORT).show()
        }
    }
}