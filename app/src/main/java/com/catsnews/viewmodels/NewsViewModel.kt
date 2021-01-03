package com.catsnews.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catsnews.NewsApp
import com.catsnews.constants.Resource
import com.catsnews.models.Article
import com.catsnews.models.Response
import com.catsnews.repository.NewsRepository
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel(
        app: Application,
        val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val news: MutableLiveData<Resource<Response>> = MutableLiveData()
    var newsPage = 1
    var newsResponse: Response? = null

    init {
        getNews("ru")
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        safeNewsCall(countryCode)


    }

    private fun handleNewsResponse(response: retrofit2.Response<Response>): Resource<Response> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                newsPage++
                if (newsResponse == null) {
                    newsResponse = resultResponse
                } else {
                    val oldArticle = newsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Succes(newsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeNewsCall(countryCode: String) {
        news.postValue(Resource.Loading())
        try {
            if (checkInternet()) {
                val response = newsRepository.getNews(countryCode, newsPage)
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
        val connectivityManager = getApplication<NewsApp>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= 23) {//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capability =
                    connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_ETHERNET) -> true
                capability.hasTransport(TRANSPORT_CELLULAR)->true
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