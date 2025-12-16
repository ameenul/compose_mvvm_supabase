package com.example.cobasupabase.domain.mapper

import com.example.cobasupabase.data.dto.NewsDto
import com.example.cobasupabase.domain.model.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object NewsMapper {

    private val isoDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    fun map(dto: NewsDto): News {

        var publishedDate: Date = Date()
        dto.datePublished?.let { dateString ->
            try {
                publishedDate = isoDateTimeFormat.parse(dateString) ?: Date()
            } catch (e: Exception) {
                println("Error parsing datePublished: ${e.message}")
                publishedDate = Date()
            }
        }

        var createdAtDate: Date = Date()
        dto.createdAt?.let { dateString ->
            try {
                createdAtDate = isoDateTimeFormat.parse(dateString) ?: Date()
            } catch (e: Exception) {
                println("Error parsing createdAt: ${e.message}")
                createdAtDate = Date()
            }
        }


        val newsId = dto.id ?: throw IllegalStateException("News ID cannot be null from DTO")
        val newsUserId = dto.userId ?: throw IllegalStateException("User ID cannot be null from DTO")

        return News(
            id = newsId,
            userId = newsUserId,
            title = dto.title,
            content = dto.content,
            author = dto.author,
            datePublished = publishedDate,
            imageUrl = dto.imageUrl ?: "https://via.placeholder.com/600x400?text=No+Image",
            createdAt = createdAtDate
        )
    }
}