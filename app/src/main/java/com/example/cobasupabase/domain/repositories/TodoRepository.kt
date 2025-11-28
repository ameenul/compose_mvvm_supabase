package com.example.cobasupabase.domain.repositories




import com.example.cobasupabase.data.remote.SupabaseHolder
import com.example.cobasupabase.domain.mapper.TodoMapper
import com.example.cobasupabase.domain.model.Todo
import com.example.cobasupabase.domain.model.TodoDto
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class TodoRepository {

    private val postgrest get() = SupabaseHolder.client.postgrest
    private val storage get() = SupabaseHolder.client.storage.from("todo-images")

    private fun resolveImageUrl(path: String?): String? {
        if (path == null) return null
        // Bila bucket private gunakan signed URL:
        return storage.publicUrl(path)
    }

    suspend fun fetchTodos(): List<Todo> {
        val userId = SupabaseHolder.session()?.user?.id ?: return emptyList()
        val response = postgrest["todos_a"].select {
            filter {
                eq("user_id", userId)
            }
            order("created_at", Order.DESCENDING)
        }
        val list = response.decodeList<TodoDto>()
        return list.map { TodoMapper.map(it, ::resolveImageUrl) }
    }

    suspend fun addTodo(
        title: String,
        description: String?,
        imageFile: File?
    ): Todo {

        val userId = SupabaseHolder.session()?.user?.id
            ?: throw IllegalStateException("User not logged in")


        var imagePath: String? = null
        if (imageFile != null) {
            imagePath = uploadImage(imageFile, userId)
        }

        val insert = postgrest["todos_a"].insert(
            mapOf(
                "user_id" to userId,
                "title" to title,
                "description" to description,
                "image_url" to imagePath
            )
        ) { select() }

        val dto = insert.decodeSingle<TodoDto>()
        return TodoMapper.map(dto, ::resolveImageUrl)
    }

//    private suspend fun uploadImage(file: File): String = withContext(Dispatchers.IO) {
//        val objectName = "${UUID.randomUUID()}_${file.name}"
//        storage.upload(objectName, file.readBytes())
//        objectName
//    }

    private suspend fun uploadImage(file: File, uid: String): String = withContext(Dispatchers.IO) {
        // Simpan di folder milik user agar lolos policy: name LIKE auth.uid() || '/%'
        val objectName = "$uid/${UUID.randomUUID()}_${file.name}"
        storage.upload(objectName, file.readBytes())
        objectName // ini yang disimpan ke kolom image_path
    }

    suspend fun deleteTodo(id: String) {
        postgrest["todos_a"].delete {
            filter { eq("id", id) }
        }
    }



//    suspend fun getTodo(id: String): Todo? {
//        val response = postgrest["todos_a"].select {
//            filter { eq("id", id) }
//            single()
//        }
//        android.util.Log.d("GET_TODO", "raw=" + (response.data ?: "null"))
//        val dto = response.decodeSingle<TodoDto>()
//        return TodoMapper.map(dto, ::resolveImageUrl)
//    }

    suspend fun getTodo(id: String): Todo? {
        val response = postgrest["todos_a"].select {
            filter { eq("id", id) }
            // JANGAN pakai single() di sini
        }
        android.util.Log.d("GET_TODO", "raw=" + (response.data ?: "null"))
        val dto = response.decodeList<TodoDto>().firstOrNull() ?: return null
        return TodoMapper.map(dto, ::resolveImageUrl)
    }

}