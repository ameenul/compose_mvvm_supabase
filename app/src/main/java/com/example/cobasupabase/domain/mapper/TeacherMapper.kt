package com.example.cobasupabase.domain.mapper

import com.example.cobasupabase.data.dto.TeacherDto
import com.example.cobasupabase.domain.model.Teacher

object TeacherMapper {
    fun map(dto: TeacherDto): Teacher {
        return Teacher(
            id = dto.id,
            userId = dto.userId ?: "",
            name = dto.name,
            subject = dto.subject,
            description = dto.description,
            imageUrl = dto.imageUrl ?: "https://via.placeholder.com/150",
            rating = dto.rating ?: 0.0,
            price = dto.price ?: "Free",
            educationHistory = dto.educationHistory,
            phoneNumber = dto.phoneNumber,
            certifications = dto.certifications,
            experience = dto.experience,
            linkedinUrl = dto.linkedinUrl,
            educationTags = dto.educationLevel?.split(",")?.map { it.trim() } ?: emptyList()
        )
    }
}