package com.catsnews.domain.usecase

import com.catsnews.domain.entity.NewsResponse
import com.catsnews.domain.repository.NewsRepository
import retrofit2.Response
import javax.inject.Inject


class GetNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        newsRepository.getNews(countryCode, pageNumber)
}