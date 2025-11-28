package com.example.cobasupabase.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.domain.model.Todo
import com.example.cobasupabase.domain.repositories.TodoRepository
import com.example.cobasupabase.ui.common.UiResult

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class TodoViewModel(
    private val repo: TodoRepository = TodoRepository()
) : ViewModel() {

    private val _todos = MutableStateFlow<UiResult<List<Todo>>>(UiResult.Loading)
    val todos: StateFlow<UiResult<List<Todo>>> = _todos

    private val _adding = MutableStateFlow<UiResult<Todo>?>(null)
    val adding: StateFlow<UiResult<Todo>?> = _adding

    fun loadTodos() {
        _todos.value = UiResult.Loading
        viewModelScope.launch {
            try {
                val list = repo.fetchTodos()
                _todos.value = UiResult.Success(list)
            } catch (e: Exception) {
                _todos.value = UiResult.Error(e.message ?: "Gagal memuat")
            }
        }
    }

    fun addTodo(title: String, description: String?, imageFile: File?) {
        _adding.value = UiResult.Loading
        viewModelScope.launch {
            try {
                val todo = repo.addTodo(title, description, imageFile)
                _adding.value = UiResult.Success(todo)
                loadTodos()
            } catch (e: Exception) {
                _adding.value = UiResult.Error(e.message ?: "Tambah gagal")
            }
        }
    }

    fun deleteTodo(id: String) {
        viewModelScope.launch {
            try {
                repo.deleteTodo(id)
                loadTodos()
            } catch (_: Exception) { }
        }
    }

    suspend fun getTodo(id: String): Todo? = repo.getTodo(id)
}