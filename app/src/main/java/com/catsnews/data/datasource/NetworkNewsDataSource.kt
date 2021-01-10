package com.catsnews.data.datasource

import com.catsnews.data.network.NewsApi
import com.catsnews.domain.entity.NewsResponse
import retrofit2.Response
import javax.inject.Inject

interface NetworkNewsDataSource {
    suspend fun getNews(countryCode: String, pageNumber: Int): Response<NewsResponse>
}

class NetworkNewsDataSourceImpl @Inject constructor(
    private val newsNewsApiClient: NewsApi
) : NetworkNewsDataSource {

    override suspend fun getNews(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        newsNewsApiClient.getNews(countryCode, pageNumber)
}
