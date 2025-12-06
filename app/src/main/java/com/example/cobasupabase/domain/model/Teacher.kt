package com.example.cobasupabase.domain.model

data class Teacher(
    val id: Int,
    val name: String,
    val subject: String,
    val rating: Double,
    val price: String,
    val educationTags: List<String>, // Kita ubah String "SD, SMP" jadi List
    val imageUrl: String
)