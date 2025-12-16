package com.example.cobasupabase.data.repositories

import com.example.cobasupabase.data.dto.NewsDto
import com.example.cobasupabase.data.remote.SupabaseHolder
import com.example.cobasupabase.domain.mapper.NewsMapper
import com.example.cobasupabase.domain.model.News
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.storage.storage


class NewsRepository {
    private val postgrest get() = SupabaseHolder.client.postgrest
    private val auth get() = SupabaseHolder.client.auth
    private val storage get() = SupabaseHolder.client.storage

    suspend fun getNews(): List<News> {
        val response = postgrest["news"].select()
        val listDto = response.decodeList<NewsDto>()
        return listDto.map { NewsMapper.map(it) }
    }

    suspend fun getNewsById(id: Int): News {
        val response = postgrest["news"].select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<NewsDto>()
        return NewsMapper.map(response)
    }

    suspend fun deleteNews(id: Int) {
        postgrest["news"].delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun updateNews(id: Int, title: String, content: String, author: String, imageUrl: String) {
        postgrest["news"].update({
            set("title", title)
            set("content", content)
            set("author", author)
            set("image_url", imageUrl)
        }) {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun createNews(title: String, content: String, author: String, imageUrl: String) {
        val currentUser = auth.currentUserOrNull()
            ?: throw Exception("User belum login! Tidak bisa membuat berita.")

        val newNews = NewsDto(
            userId = currentUser.id,
            title = title,
            content = content,
            author = author,
            imageUrl = imageUrl,
            datePublished = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US).format(java.util.Date()),
            createdAt = null // Biarkan null, biar Supabase yang generate timestamp server side (opsional)
        )

        // 3. Kirim ke Supabase
        postgrest["news"].insert(newNews)
    }

    // upload Gambar untuk createNews
    suspend fun uploadImage(byteArray: ByteArray): String {
        val currentUser = auth.currentUserOrNull()
            ?: throw Exception("User belum login! Tidak bisa mengupload gambar.")

        val fileName = "${currentUser.id}/news_${System.currentTimeMillis()}.jpg"
        val bucket = storage["news-images"]

        bucket.upload(fileName, byteArray)
        return bucket.publicUrl(fileName)
    }
}