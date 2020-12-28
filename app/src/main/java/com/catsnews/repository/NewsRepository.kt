package com.catsnews.repository

import com.catsnews.api.Retrofit
import com.catsnews.database.ArticleDataBase
import com.catsnews.models.Article

class NewsRepository (
    val database:ArticleDataBase
){
    suspend fun getNews(countryCode:String,pageNumber:Int)=
        Retrofit.api.getNews(countryCode,pageNumber)

    suspend fun upsert(article:Article)=database.getArticleDao().upsert(article)

    fun getSavedNews()=database.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=database.getArticleDao().deleteArticles(article)
}