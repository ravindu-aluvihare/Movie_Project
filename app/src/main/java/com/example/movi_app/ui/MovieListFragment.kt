package com.example.movi_app.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movi_app.RetrofitInstance
import com.example.movi_app.adapter.MovieAdapter
import com.example.movi_app.data.MovieRepository
import com.example.movi_app.databinding.FragmentMovieListBinding
import com.example.movi_app.model.Movie

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter

    private var allMovies: List<Movie> = emptyList()

    private var selectedGenreButton: View? = null

    private val viewModel by lazy {
        val repository = MovieRepository(RetrofitInstance.api)
        val factory = MovieListViewModelFactory(repository)
        ViewModelProvider(this, factory)[MovieListViewModel::class.java]
    }

    // Map genres to their IDs (replace these with your actual genre IDs)
    private val genreNameToId = mapOf(
        "Adventure" to 12,
        "Action" to 28,
        "Comedy" to 35,
        "Documentary" to 99,
        "Crime" to 80,
        "Drama" to 18,
        "Romance" to 10749
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genreButtons = listOf(
            binding.buttonShowAll to null,
            binding.button0 to "Adventure",
            binding.button1 to "Action",
            binding.button2 to "Comedy",
            binding.button3 to "Documentary",
            binding.button4 to "Crime",
            binding.button5 to "Drama",
            binding.button7 to "Romance"
        )

        genreButtons.forEach { (button, genreName) ->
            button.setOnClickListener {
                // Deselect previous button
                selectedGenreButton?.isSelected = false

                // Select current button
                button.isSelected = true
                selectedGenreButton = button

                // Apply filter
                if (genreName == null) {
                    movieAdapter.submitList(allMovies)
                } else {
                    filterMovies(genreName)
                }
            }
        }


        movieAdapter = MovieAdapter { movie ->
            navigateToMovieDetail(movie)
        }

        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMovies.adapter = movieAdapter

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            allMovies = movies
            movieAdapter.submitList(movies)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            movieAdapter.submitList(searchResults)
        }


        // Listen for search input changes
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotBlank()) {
                    viewModel.searchMovies(query)
                } else {
                    movieAdapter.submitList(allMovies) // show all movies if query is empty
                    selectedGenreButton?.isSelected = false
                    binding.buttonShowAll.isSelected = true
                    selectedGenreButton = binding.buttonShowAll
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Set Show All button selected on startup
        binding.buttonShowAll.isSelected = true
        selectedGenreButton = binding.buttonShowAll


    }

    private fun navigateToMovieDetail(movie: Movie) {
        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie.id)
        findNavController().navigate(action)
    }

    private fun filterMovies(genreName: String) {
        val genreId = genreNameToId[genreName]
        if (genreId == null) {
            // If genre not found, show all movies
            movieAdapter.submitList(allMovies)
            return
        }
        val filteredList = allMovies.filter { movie ->
            movie.genreIds.contains(genreId)
        }
        movieAdapter.submitList(filteredList)
    }

    private fun filterBySearchQuery(query: String) {
        val filteredList = if (query.isEmpty()) {
            allMovies
        } else {
            allMovies.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }
        }
        movieAdapter.submitList(filteredList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
