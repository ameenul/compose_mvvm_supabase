package com.example.cobasupabase.domain.model

import java.util.Date

data class News(
    val id: Int,
    val userId: String,
    val title: String,
    val content: String,
    val author: String,
    val datePublished: Date,
    val imageUrl: String,
    val createdAt: Date = Date()
)