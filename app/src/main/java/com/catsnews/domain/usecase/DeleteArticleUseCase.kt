package com.catsnews.domain.usecase

import com.catsnews.domain.entity.Article
import com.catsnews.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteArticleUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(article: Article) =
        newsRepository.deleteArticle(article)
}