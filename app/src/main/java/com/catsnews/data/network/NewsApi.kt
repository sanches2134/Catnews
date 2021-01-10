package com.catsnews.data.network

import com.catsnews.data.network.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getNews(
            @Query("country")
            countryCode: String = "ru",
            @Query("page")
            pageNumber: Int = 1,
            @Query("apiKey")
            apiKey: String = API_KEY
    ): Response<com.catsnews.domain.entity.NewsResponse>

}