package com.catsnews.models

data class Response (
        val articles: MutableList<Article>,
        val status: String,
        val totalResults: Int
)