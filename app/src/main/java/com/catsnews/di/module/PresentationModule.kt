package com.catsnews.di.module

import android.content.Context
import com.catsnews.domain.usecase.DeleteArticleUseCase
import com.catsnews.domain.usecase.GetNewsUseCase
import com.catsnews.domain.usecase.GetSavedNewsUseCase
import com.catsnews.domain.usecase.UpsertUseCase
import com.catsnews.presentation.viewmodel.NewsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class PresentationModule {

    @Singleton
    @Provides
    fun provideNewsViewModel(
        getNewsUseCase: GetNewsUseCase,
        deleteArticleUseCase: DeleteArticleUseCase,
        getSavedNewsUseCase: GetSavedNewsUseCase,
        upsertUseCase: UpsertUseCase,
        context: Context
    ): NewsViewModel = NewsViewModel(
        getNewsUseCase,
        deleteArticleUseCase,
        getSavedNewsUseCase,
        upsertUseCase,
        context
    )
}
