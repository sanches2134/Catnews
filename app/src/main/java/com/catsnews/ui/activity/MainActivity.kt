package com.catsnews.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.catsnews.catnews.R
import com.catsnews.database.ArticleDataBase
import com.catsnews.repository.NewsRepository
import com.catsnews.viewmodels.NewsViewModel
import com.catsnews.viewmodels.newsVMProviderFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newsrepository=NewsRepository(ArticleDataBase(this))
        val viewModelProviderFactory=newsVMProviderFactory(application,newsrepository)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }

}