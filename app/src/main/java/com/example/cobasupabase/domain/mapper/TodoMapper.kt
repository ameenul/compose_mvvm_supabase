package com.example.cobasupabase.domain.mapper



import com.example.cobasupabase.domain.model.Todo
import com.example.cobasupabase.domain.model.TodoDto
import java.time.Instant


object TodoMapper {
    fun map(dto: TodoDto, imageUrlResolver: (String?) -> String?): Todo =
        Todo(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            imageUrl = imageUrlResolver(dto.image_url),
            createdAt = Instant.parse(dto.created_at)
        )
}