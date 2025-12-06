package com.example.cobasupabase.data.repositories

import com.example.cobasupabase.data.dto.TeacherDto
import com.example.cobasupabase.data.remote.SupabaseHolder
import com.example.cobasupabase.domain.mapper.TeacherMapper
import com.example.cobasupabase.domain.model.Teacher
import io.github.jan.supabase.postgrest.postgrest
// import io.github.jan.supabase.storage.storage // Uncomment jika nanti butuh storage

class TeacherRepository {

    // Mengikuti gaya dosen: Menggunakan properti extension 'postgrest'
    private val postgrest get() = SupabaseHolder.client.postgrest

    // Jika nanti gambar guru diambil dari bucket storage sendiri, aktifkan ini:
    // private val storage get() = SupabaseHolder.client.storage.from("mentorify-public")

    suspend fun getTeachers(): List<Teacher> {
        // PERBEDAAN PENTING:
        // TodoRepository pakai filter user_id (karena data pribadi).
        // TeacherRepository JANGAN pakai filter user_id, karena ini data PUBLIK (Siswa harus bisa lihat semua guru).

        val response = postgrest["teachers"].select() // Ambil semua data guru

        val listDto = response.decodeList<TeacherDto>()

        // Menggunakan Mapper object seperti contoh TodoMapper
        return listDto.map { TeacherMapper.map(it) }
    }
}