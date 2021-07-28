package com.example.test.screen.jokes

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test.R
import com.example.test.databinding.JokesScreenBinding
import com.example.test.feature.arch.viewModelsExt
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JokesFragment : Fragment(R.layout.jokes__screen) {
    @Inject
    lateinit var viewModelFactory: JokesViewModel.Factory

    private val binding by viewBinding(JokesScreenBinding::bind)
    private lateinit var adapter: JokesAdapter

    private val viewModel: JokesViewModel by viewModelsExt {
        viewModelFactory.create(getString(R.string.core__network_error), getString(R.string.jokes__api_error))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setColorSchemeResources(R.color.core__swipe_refresh)
        adapter = JokesAdapter(getString(R.string.jokes__id_format))

        binding.jokesList.also { jokesList ->
            jokesList.adapter = adapter

            jokesList.addItemDecoration(
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

        viewModel.jokes.observe(viewLifecycleOwner) {
            binding.jokesList.visibility = if (it != null) View.VISIBLE else View.INVISIBLE
            adapter.source = it ?: emptyList()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
