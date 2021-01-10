package com.catsnews.data.repository

import androidx.lifecycle.LiveData
import com.catsnews.data.datasource.LocalNewsDataSource
import com.catsnews.data.datasource.NetworkNewsDataSource
import com.catsnews.domain.repository.NewsRepository
import com.catsnews.domain.entity.Article
import com.catsnews.domain.entity.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val networkNewsDataSource: NetworkNewsDataSource,
    private val localNewsDataSource: LocalNewsDataSource
) : NewsRepository {
    override suspend fun getNews(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        networkNewsDataSource.getNews(countryCode, pageNumber)


    override suspend fun upsert(article: Article): Long = localNewsDataSource.upsert(article)

    override fun getSavedNews(): LiveData<List<Article>> = localNewsDataSource.getSavedNews()

    override suspend fun deleteArticle(article: Article) =
        localNewsDataSource.deleteArticle(article)


}