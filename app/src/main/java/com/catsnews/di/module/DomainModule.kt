package com.catsnews.di.module


import com.catsnews.domain.repository.NewsRepository
import com.catsnews.domain.usecase.DeleteArticleUseCase
import com.catsnews.domain.usecase.GetNewsUseCase
import com.catsnews.domain.usecase.GetSavedNewsUseCase
import com.catsnews.domain.usecase.UpsertUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideGetNewsUseCase(
        newsRepository: NewsRepository
    ): GetNewsUseCase = GetNewsUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideGetSavedNewsUseCase(
        newsRepository: NewsRepository
    ): GetSavedNewsUseCase = GetSavedNewsUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideUpsertUseCase(
        newsRepository: NewsRepository
    ): UpsertUseCase = UpsertUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideDeleteArticleUseCase(
        newsRepository: NewsRepository
    ): DeleteArticleUseCase = DeleteArticleUseCase(newsRepository)

}