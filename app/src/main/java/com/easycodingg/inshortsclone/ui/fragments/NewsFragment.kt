package com.easycodingg.inshortsclone.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.easycodingg.inshortsclone.R
import com.easycodingg.inshortsclone.adapters.NewsViewPagerAdapter
import com.easycodingg.inshortsclone.databinding.FragmentNewsBinding
import com.easycodingg.inshortsclone.ui.viewmodels.NewsViewModel
import com.easycodingg.inshortsclone.util.Resource
import com.easycodingg.inshortsclone.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment: Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val viewModel: NewsViewModel by viewModels()
    private val args: NewsFragmentArgs by navArgs()

    private lateinit var newsAdapter: NewsViewPagerAdapter
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()

        viewModel.queryType = args.queryType
        viewModel.queryValue = args.queryValue

        viewModel.onFragmentInForeground()

        viewModel.articles.observe(viewLifecycleOwner) { response ->
            binding.pbLoading.isVisible = response is Resource.Loading
            binding.btnRetry.isVisible = response is Resource.Error && response.data?.size == 0
            binding.tvCheckConnection.isVisible = response is Resource.Error && response.data?.size == 0

            if (response !is Resource.Error) {
                response.data?.let {
                    newsAdapter.submitList(it)
                    newsAdapter.notifyDataSetChanged()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when(event) {
                    is NewsViewModel.Event.ShowErrorMessage -> {
                        val errorMessage = when(event.error) {
                            is HttpException -> "No Internet Connection"
                            else -> "Unknown Error Occurred"
                        }
                        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnRetry.setOnClickListener {
            viewModel.onRetryClicked()
        }

    }

    private fun setupViewPager() {
        newsAdapter = NewsViewPagerAdapter(listOf()) { article ->
            viewModel.onBookmarkButtonClicked(article)
        }

        /*To check for viewpager last item*/
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if(!newsAdapter.currentList().isNullOrEmpty() &&
                        newsAdapter.currentList()[position] == newsAdapter.currentList().last()) {

                        viewModel.onViewPagerLastItem()
                }
            }
        }
        binding.vpNews.apply {
            adapter = newsAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            registerOnPageChangeCallback(pageChangeCallback)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.vpNews.unregisterOnPageChangeCallback(pageChangeCallback)
    }

}