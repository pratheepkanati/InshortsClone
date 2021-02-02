package com.easycodingg.inshortsclone.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.easycodingg.inshortsclone.R
import com.easycodingg.inshortsclone.adapters.QueryAdapter
import com.easycodingg.inshortsclone.databinding.FragmentDiscoverBinding
import com.easycodingg.inshortsclone.ui.viewmodels.DiscoverViewModel
import com.easycodingg.inshortsclone.util.Constants.SEARCH_QUERY_TYPE
import com.easycodingg.inshortsclone.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DiscoverFragment: Fragment(R.layout.fragment_discover) {

    private val binding by viewBinding(FragmentDiscoverBinding::bind)
    private val viewModel: DiscoverViewModel by viewModels()

    private lateinit var queryAdapter: QueryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.etSearchNews.apply {
            setOnKeyListener { _, keyCode, event ->
                if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    val searchQuery = this.text.trim().toString()
                    val action = DiscoverFragmentDirections
                            .actionDiscoverFragmentToNewsFragment(SEARCH_QUERY_TYPE, searchQuery)
                    findNavController().navigate(action)

                    return@setOnKeyListener true
                }
                false
            }
        }
    }

    private fun setupRecyclerView() {
        queryAdapter = QueryAdapter(viewModel.queryList) { query ->

            Timber.d("Type - ${query.queryType}, Value - ${query.queryValue}")
            val action = DiscoverFragmentDirections
                    .actionDiscoverFragmentToNewsFragment(query.queryType, query.queryValue)
            findNavController().navigate(action)
        }

        binding.rvTypes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = queryAdapter
        }
    }
}