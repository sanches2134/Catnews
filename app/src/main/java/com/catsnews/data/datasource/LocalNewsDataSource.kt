package com.catsnews.data.datasource

import androidx.lifecycle.LiveData
import com.catsnews.data.database.ArticleDao
import com.catsnews.domain.entity.Article
import javax.inject.Inject

interface LocalNewsDataSource {

    suspend fun upsert(article: Article): Long

    fun getSavedNews(): LiveData<List<Article>>

    suspend fun deleteArticle(article: Article)
}

class LocalNewsDataSourceImpl @Inject constructor(
    private val newsDao: ArticleDao
) : LocalNewsDataSource {

    override suspend fun upsert(article: Article): Long = newsDao.upsert(article)

    override fun getSavedNews(): LiveData<List<Article>> = newsDao.getAllArticles()

    override suspend fun deleteArticle(article: Article) = newsDao.deleteArticles(article)

}
