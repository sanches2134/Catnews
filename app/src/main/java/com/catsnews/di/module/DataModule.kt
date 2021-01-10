package com.catsnews.di.module

import android.content.Context
import androidx.room.Room
import com.catsnews.data.network.NewsApi
import com.catsnews.data.datasource.LocalNewsDataSource
import com.catsnews.data.datasource.LocalNewsDataSourceImpl
import com.catsnews.data.datasource.NetworkNewsDataSource
import com.catsnews.data.datasource.NetworkNewsDataSourceImpl
import com.catsnews.data.repository.NewsRepositoryImpl
import com.catsnews.data.database.ArticleDao
import com.catsnews.data.database.ArticleDataBase
import com.catsnews.data.network.Constants.BASE_URL
import com.catsnews.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        networkNewsDataSource: NetworkNewsDataSource,
        localNewsDataSource: LocalNewsDataSource
    ): NewsRepository =
        NewsRepositoryImpl(networkNewsDataSource, localNewsDataSource)

    @Singleton
    @Provides
    fun provideNetworkNewsDataSource(newsApiClient: NewsApi): NetworkNewsDataSource =
        NetworkNewsDataSourceImpl(newsApiClient)

    @Singleton
    @Provides
    fun provideLocalNewsDataSource(newsDao: ArticleDao): LocalNewsDataSource =
        LocalNewsDataSourceImpl(newsDao)

    @Singleton
    @Provides
    fun provideNewsDao(context: Context): ArticleDao =
        Room.databaseBuilder(
            context.applicationContext,
            ArticleDataBase::class.java,
            "article_db.db"
        ).build().getArticleDao()


    @Singleton
    @Provides
    fun provideNewsApiClient(): NewsApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NewsApi::class.java)
    }


}