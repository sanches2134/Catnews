package com.catsnews.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.catsnews.NewsApp
import com.catsnews.catnews.R
import com.catsnews.presentation.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Screen)
        setContentView(R.layout.activity_main)

        NewsApp.component.injectMainActivity(this)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }

}