package com.catsnews.presentation.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catsnews.data.network.Resource
import com.catsnews.domain.entity.Article
import com.catsnews.domain.entity.NewsResponse
import com.catsnews.domain.usecase.DeleteArticleUseCase
import com.catsnews.domain.usecase.GetNewsUseCase
import com.catsnews.domain.usecase.GetSavedNewsUseCase
import com.catsnews.domain.usecase.UpsertUseCase
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val upsertUseCase: UpsertUseCase,
    private val context: Context
) : ViewModel() {

    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1
    private var newsNewsResponse: NewsResponse? = null

    init {
        getNews("ru")
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        safeNewsCall(countryCode)
    }

    private fun handleNewsResponse(newsResponse: retrofit2.Response<NewsResponse>): Resource<NewsResponse> {
        if (newsResponse.isSuccessful) {
            newsResponse.body()?.let { resultResponse ->
                newsPage++
                if (this.newsNewsResponse == null) {
                    this.newsNewsResponse = resultResponse
                } else {
                    val oldArticle = this.newsNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(this.newsNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(newsResponse.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        upsertUseCase(article)
    }

    fun getSavedNews() = getSavedNewsUseCase()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        deleteArticleUseCase(article)
    }

    private suspend fun safeNewsCall(countryCode: String) {
        news.postValue(Resource.Loading())
        try {
            if (checkInternet()) {
                val response = getNewsUseCase(countryCode, newsPage)
                news.postValue(handleNewsResponse(response))
            } else {
                news.postValue(Resource.Error("Нет подключения к интернету!("))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> news.postValue(Resource.Error("Ошибка соединения..."))
                else -> news.postValue(Resource.Error("Ошибка преобразования..."))
            }

        }
    }

    private fun checkInternet(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= 23) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capability =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_ETHERNET) -> true
                capability.hasTransport(TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        return false
    }
}