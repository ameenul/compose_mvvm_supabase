package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val id: Int,
    @SerialName("user_id")
    val userId: String,
    val name: String,
    val address: String,
    val rating: Double?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("created_at")
    val createdAt: String
)
