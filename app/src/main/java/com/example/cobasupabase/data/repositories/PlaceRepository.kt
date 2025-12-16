package com.example.cobasupabase.data.repositories

import com.example.cobasupabase.data.dto.PlaceDto
import com.example.cobasupabase.data.remote.SupabaseHolder
import com.example.cobasupabase.domain.mapper.PlaceMapper
import com.example.cobasupabase.domain.model.Place
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage

class PlaceRepository {

    private val client = SupabaseHolder.client

    /** READ */
    suspend fun getPlaces(): List<Place> {
        val response = client.postgrest["places"]
            .select {
                order(
                    "created_at",
                    io.github.jan.supabase.postgrest.query.Order.DESCENDING
                )
            }
            .decodeList<PlaceDto>()

        return response.map { PlaceMapper.map(it) }
    }

    /** CREATE */
    /** CREATE */
    suspend fun createPlace(
        userId: String,
        name: String,
        address: String,
        rating: Double,
        imageUrl: String? = null
    ) {
        // Jika sebelumnya ada logika upload imageBytes, hapus / sesuaikan
        // Sekarang kita pakai imageUrl langsung dari input
        client.postgrest["places"].insert(
            mapOf(
                "user_id" to userId,
                "name" to name,
                "address" to address,
                "rating" to rating,
                "image_url" to imageUrl
            )
        )
    }

    /** UPDATE */
    suspend fun updatePlace(
        id: Int,
        name: String,
        address: String,
        rating: Double
    ) {
        client.postgrest["places"].update(
            {
                set("name", name)
                set("address", address)
                set("rating", rating)
            }
        ) {
            filter { eq("id", id) }
        }
    }

    /** DELETE */
    suspend fun deletePlace(id: Int) {
        client.postgrest["places"].delete {
            filter { eq("id", id) }
        }
    }
    suspend fun getPlaceById(id: Int): Place? {
        return try {
            val allPlaces = getPlaces() // ambil semua tempat dari Supabase
            allPlaces.find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
}
