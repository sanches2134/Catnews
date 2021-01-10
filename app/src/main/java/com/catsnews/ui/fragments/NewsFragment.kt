package com.catsnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catsnews.ui.activity.MainActivity
import com.catsnews.ui.adapter.NewsAdapter
import com.catsnews.catnews.R
import com.catsnews.data.network.Constants.QUERY_PAGE_SIZE
import com.catsnews.data.network.Resource
import com.catsnews.presentation.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.item_error.*



class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        bindRV()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("Article", it)
                putSerializable("previous",1)
            }
            findNavController().navigate(

                    R.id.action_newsFragment_to_webFragment,
                    bundle
            )
        }

        viewModel.news.observe(viewLifecycleOwner,  { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    resource.data?.let { request ->
                        newsAdapter.differ.submitList(request.articles.toList())
                        val totalPages = (request.totalResults?.div(QUERY_PAGE_SIZE)?.plus(2))
                        lastPage = viewModel.newsPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let {
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
        loading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        loading = true
    }

    private fun hideErrorMessage() {
        itemErrorMessage.visibility = View.INVISIBLE
        error = false
    }

    private fun showErrorMessage(message: String) {
        itemErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
        this.error = true
    }

    var loading = false
    private var error = false
    var lastPage = false
    var scrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItem = layoutManager.findFirstVisibleItemPosition()
            val itemCount = layoutManager.itemCount

            val isNotLoadLastPage = !loading && !lastPage
            val isAtLastItem = firstItem >= 0
            val isNotAtBeginning = firstItem >= QUERY_PAGE_SIZE
            val isTotalMoreThanVisible = itemCount >= QUERY_PAGE_SIZE
            val pagination = isNotLoadLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    scrolling
            if (pagination) {
                viewModel.getNews("ru")
                scrolling = false
            } else {
                rvBreakingNews.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                scrolling = true
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