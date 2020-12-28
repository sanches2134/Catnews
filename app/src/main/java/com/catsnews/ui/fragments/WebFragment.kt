package com.catsnews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.catsnews.ui.activity.MainActivity
import com.catsnews.catnews.R
import com.catsnews.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : Fragment(R.layout.fragment_web) {

    lateinit var viewModel: NewsViewModel
    val args: WebFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        val article=args.article
        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Новость сохранена!", Snackbar.LENGTH_SHORT).show()
        }
    }
}