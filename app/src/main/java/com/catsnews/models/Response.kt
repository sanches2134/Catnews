package com.catsnews.models

data class Response(
        val articles: MutableList<Article>,
        val status: String?=null,
        val totalResults: Int?=null
)