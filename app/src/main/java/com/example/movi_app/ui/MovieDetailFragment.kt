package com.example.movi_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.movi_app.RetrofitInstance
import com.example.movi_app.data.MovieRepository
import com.example.movi_app.databinding.FragmentMovieDetailBinding
import com.squareup.picasso.Picasso

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var repository: MovieRepository

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = MovieRepository(RetrofitInstance.api)

        val factory = MovieDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MovieDetailViewModel::class.java]

        val movieId = args.movieId
        viewModel.fetchMovieDetails(movieId)

        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding.apply {
                Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.posterPath}").into(imagePoster)
                textTitle.text = movie.title
                textReleaseDate.text = "Release Date: ${movie.releaseDate}"
                textRating.text = "Rating: ${movie.rating}"
                textOverview.text = "Overview: ${movie.overview}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}