package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherDto(
    val id: Int,

    @SerialName("user_id")
    val userId: String? = null,

    @SerialName("created_at")
    val createdAt: String,

    val name: String,
    val subject: String,

    @SerialName("education_history")
    val educationHistory: String,

    @SerialName("phone_number")
    val phoneNumber: String,

    val description: String,

    // Optional Fields
    val certifications: String? = null,
    val experience: String? = null,

    @SerialName("linkedin_url")
    val linkedinUrl: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("education_level")
    val educationLevel: String? = null,

    val price: String? = null,
    val rating: Double? = 0.0
)