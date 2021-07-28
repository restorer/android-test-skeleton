package com.example.test.screen.imdb

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.RequestManager
import com.example.test.R
import com.example.test.databinding.ImdbScreenBinding
import com.example.test.feature.arch.viewModelsExt
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImdbFragment : Fragment(R.layout.imdb__screen) {
    @Inject
    lateinit var viewModelFactory: ImdbViewModel.Factory

    @Inject
    lateinit var glide: RequestManager

    private val binding by viewBinding(ImdbScreenBinding::bind)
    private lateinit var adapter: ImdbAdapter

    private val viewModel by viewModelsExt {
        viewModelFactory.create(getString(R.string.core__network_error), getString(R.string.imdb__api_error))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setColorSchemeResources(R.color.core__swipe_refresh)
        adapter = ImdbAdapter(glide)

        binding.moviesList.also { moviesList ->
            moviesList.adapter = adapter

            moviesList.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).also {
                    it.setDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.core__divider)))
                }
            )
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            binding.errorMessage.also {
                it.visibility = if (message != null) View.VISIBLE else View.GONE
                it.text = message ?: ""
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            binding.moviesList.visibility = if (it != null) View.VISIBLE else View.INVISIBLE
            adapter.source = it ?: emptyList()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
