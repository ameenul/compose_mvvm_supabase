package com.example.cobasupabase.domain.mapper

import com.example.cobasupabase.data.dto.PlaceDto
import com.example.cobasupabase.domain.model.Place

object PlaceMapper {
    fun map(dto: PlaceDto): Place {
        return Place(
            id = dto.id,
            name = dto.name,
            address = dto.address,
            rating = dto.rating ?: 0.0,
            imageUrl = dto.imageUrl
                ?: "https://via.placeholder.com/300"
        )
    }
}