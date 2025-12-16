package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherInsert(
    @SerialName("user_id")
    val userId: String,
    val name: String,
    val subject: String,
    @SerialName("education_history")
    val educationHistory: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val description: String,
    val certifications: String?,
    val experience: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("education_level")
    val educationLevel: String?,
    val price: String?
)