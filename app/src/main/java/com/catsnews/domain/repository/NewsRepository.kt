package com.catsnews.domain.repository

import androidx.lifecycle.LiveData
import com.catsnews.domain.entity.Article
import com.catsnews.domain.entity.NewsResponse
import retrofit2.Response

interface NewsRepository {

    suspend fun getNews(countryCode: String, pageNumber: Int): Response<NewsResponse>

    suspend fun upsert(article: Article): Long

    fun getSavedNews(): LiveData<List<Article>>

    suspend fun deleteArticle(article: Article)
}