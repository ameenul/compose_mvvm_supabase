package com.example.cobasupabase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherDto(
    val id: Int, // id di database bigint (int/long)
    val name: String,
    val subject: String,
    val rating: Double? = 0.0, // nullable jaga-jaga
    val price: String? = null,

    // Sesuaikan nama kolom di database (snake_case)
    @SerialName("education_level")
    val educationLevel: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null
)