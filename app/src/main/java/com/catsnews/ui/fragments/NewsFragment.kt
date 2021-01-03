package com.catsnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catsnews.ui.activity.MainActivity
import com.catsnews.adapters.NewsAdapter
import com.catsnews.catnews.R
import com.catsnews.constants.Constants.Companion.QUERY_PAGE_SIZE
import com.catsnews.constants.Resource
import com.catsnews.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.item_error.*



class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        bindRV()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(

                    R.id.action_newsFragment_to_webFragment,
                    bundle
            )
        }

        viewModel.news.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Succes -> {
                    hideProgressBar()
                    hideErrorMessage()
                    it.data?.let { it ->
                        newsAdapter.different.submitList(it.articles.toList())
                        val totalPages = (it.totalResults?.div(QUERY_PAGE_SIZE)?.plus(2))
                        isLastPage = viewModel.newsPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let {
                        if (it != "")
                            showErrorMessage(it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        btnRetry.setOnClickListener {
            viewModel.getNews("ru")
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
        isError = true
    }

    var isLoading = false
    var isError = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            val isNotLoadLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition >= 0
            val isNotAtBeginning = firstVisibleItemPosition >= QUERY_PAGE_SIZE
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPagination = isNotLoadLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    isScrolling
            if (shouldPagination) {
                viewModel.getNews("ru")
                isScrolling = false
            } else {
                rvBreakingNews.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun bindRV() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.scrollListener)
        }
    }


}