package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val id: Int? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    val title: String,
    val content: String,
    val author: String,
    @SerialName("date_published") val datePublished: String?,
    @SerialName("image_url") val imageUrl: String?
)