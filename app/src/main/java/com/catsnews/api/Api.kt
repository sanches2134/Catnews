package com.catsnews.api

import com.catsnews.constants.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("v2/top-headlines")
    suspend fun getNews(
            @Query("country")
            countryCode: String = "ru",
            @Query("page")
            pageNumber: Int = 1,
            @Query("apiKey")
            apiKey: String = API_KEY
    ): Response<com.catsnews.models.Response>

}