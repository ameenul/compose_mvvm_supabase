package com.example.cobasupabase.domain.model

data class Teacher(
    val id: Int,
    val userId: String,
    val name: String,
    val subject: String,
    val description: String,
    val imageUrl: String,
    val rating: Double,
    val price: String,
    val educationHistory: String,
    val phoneNumber: String,
    val certifications: String?,
    val experience: String?,
    val linkedinUrl: String?,
    // This will be mapped from education_level
    val educationTags: List<String>
)