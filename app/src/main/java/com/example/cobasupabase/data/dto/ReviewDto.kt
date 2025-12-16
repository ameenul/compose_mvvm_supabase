package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val id: Long,
    @SerialName("user_id") val userId: String,
    @SerialName("teacher_id") val teacherId: Int?,
    // Add teacherName here, it will be populated from the teacher table
    val teacherName: String? = null,
    @SerialName("reviewer_name") val reviewerName: String?,
    val rating: Int?,
    val comment: String?,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("created_at") val createdAt: String?
)

@Serializable
data class ReviewInsertDto(
    @SerialName("user_id") val userId: String,
    @SerialName("teacher_id") val teacherId: Int,
    @SerialName("reviewer_name") val reviewerName: String,
    val rating: Int,
    val comment: String,
    @SerialName("avatar_url") val avatarUrl: String?
)