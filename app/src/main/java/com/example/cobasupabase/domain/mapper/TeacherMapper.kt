package com.example.cobasupabase.domain.mapper

import com.example.cobasupabase.data.dto.TeacherDto
import com.example.cobasupabase.domain.model.Teacher

object TeacherMapper {
    fun map(dto: TeacherDto): Teacher {
        return Teacher(
            id = dto.id.toString(), // Convert Int id to String
            name = dto.name,
            subject = dto.subject,
            rating = dto.rating ?: 0.0,
            price = dto.price ?: "Gratis",

            // Logic memecah string "SD, SMP" menjadi List ["SD", "SMP"]
            educationTags = dto.educationLevel?.split(",")?.map { it.trim() } ?: emptyList(),

            // Karena data dummy kita pakai URL Unsplash (https://...), langsung pakai saja.
            // Jika nanti upload sendiri, logic resolveImageUrl bisa ditambahkan disini.
            imageUrl = dto.imageUrl ?: "https://via.placeholder.com/150"
        )
    }
}