package com.catsnews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.catsnews.ui.activity.MainActivity
import com.catsnews.catnews.R
import com.catsnews.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : Fragment(R.layout.fragment_web) {

    private lateinit var viewModel: NewsViewModel
    private val args: WebFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.Article
        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Новость сохранена!", Snackbar.LENGTH_SHORT).show()
        }
        val previousPage=args.previous
        back.setOnClickListener {
            if (previousPage==1){  findNavController().navigate(
                R.id.action_webFragment_to_newsFragment
            )}
            else
            {
                findNavController().navigate(
                    R.id.action_webFragment_to_savedNewsFragment)
            }

        }

    }
}