package com.example.movi_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movi_app.databinding.MovieItemBinding
import com.example.movi_app.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
    private val onClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) { // Extend ListAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)) // Use getItem() from ListAdapter
    }

    // No need to override getItemCount() with ListAdapter

    inner class MovieViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.posterPath}").into(imagePosterItem)
                textTitleItem.text = movie.title
                textReleaseDateItem.text = "Release Date: ${movie.releaseDate}"
                textRatingItem.text = "Rating: ${movie.rating}"
                root.setOnClickListener { onClick(movie) }
            }
        }
    }

    // DiffUtil.ItemCallback implementation
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem // Data class equality works well here
        }
    }
}