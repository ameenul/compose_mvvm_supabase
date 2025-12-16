package com.example.cobasupabase.data.repositories

import android.util.Log
import com.example.cobasupabase.data.dto.TeacherDto
import com.example.cobasupabase.data.dto.TeacherInsert
import com.example.cobasupabase.data.remote.SupabaseHolder
import com.example.cobasupabase.domain.mapper.TeacherMapper
import com.example.cobasupabase.domain.model.Teacher
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.gotrue.auth

class TeacherRepository {

    private val postgrest get() = SupabaseHolder.client.postgrest
    private val storage get() = SupabaseHolder.client.storage
    private val auth get() = SupabaseHolder.client.auth

    suspend fun getTeachers(): List<Teacher> {
        val response = postgrest["teachers"].select()
        val listDto = response.decodeList<TeacherDto>()
        return listDto.map { TeacherMapper.map(it) }
    }

    suspend fun getTeacherById(id: Int): Teacher? {
        val response = postgrest["teachers"].select {
            filter {
                eq("id", id)
            }
        }.decodeSingleOrNull<TeacherDto>()
        return response?.let { TeacherMapper.map(it) }
    }

    suspend fun createTeacher(teacherData: TeacherInsert) {
        postgrest["teachers"].insert(teacherData)
    }

    suspend fun updateTeacher(id: Int, teacherData: TeacherInsert) {
        postgrest["teachers"].update(teacherData) {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun deleteTeacher(id: Int) {
        try {
            val response = postgrest["teachers"].delete { // Capture the response
                filter {
                    eq("id", id)
                }
            }
            // Log the raw response data
            Log.d("TeacherRepository", "Delete successful for ID $id. Response: ${response.data}")
            // You can also check the status code or other properties if needed
            // Log.d("TeacherRepository", "Status code: ${response.data.status.value}")

        } catch (e: Exception) {
            // Log any errors that occur during the delete operation
            Log.e("TeacherRepository", "Error deleting teacher with ID $id: ${e.message}", e)
            throw e // Re-throw the exception if you want calling layers to handle it
        }
    }

    suspend fun uploadImage(byteArray: ByteArray, userId: String): String {
        val fileName = "${userId}/teacher_${System.currentTimeMillis()}.jpg"
        val bucket = storage["teacher-images"]

        bucket.upload(fileName, byteArray)
        return bucket.publicUrl(fileName)
    }
}