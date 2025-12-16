package com.example.cobasupabase.domain.model

data class Place(
    val id: Int,
    val name: String,
    val address: String,
    val rating: Double,
    val imageUrl: String
)
