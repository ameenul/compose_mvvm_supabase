package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherCreateDto(
    val name: String,
    val subject: String,
    @SerialName("education_history")
    val educationHistory: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val description: String,
    val certifications: String? = null,
    val experience: String? = null,
    @SerialName("linkedin_url")
    val linkedinUrl: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("education_level")
    val educationLevel: String? = null,
    val price: String? = null
    // user_id is set by default by the database.
    // rating has a default value in the database.
    // created_at is set by default by the database.
)