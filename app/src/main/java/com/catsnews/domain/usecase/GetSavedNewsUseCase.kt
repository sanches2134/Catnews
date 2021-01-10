package com.catsnews.domain.usecase

import androidx.lifecycle.LiveData
import com.catsnews.domain.entity.Article
import com.catsnews.domain.repository.NewsRepository
import javax.inject.Inject


class GetSavedNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke(): LiveData<List<Article>> =
        newsRepository.getSavedNews()
}