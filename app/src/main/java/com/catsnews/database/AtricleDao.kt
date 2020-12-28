package com.catsnews.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.catsnews.models.Article
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  upsert(atricle:Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticles(article: Article)
}