package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.TeacherRepository
import com.example.cobasupabase.domain.model.Teacher
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cobasupabase.data.remote.SupabaseHolder
import io.github.jan.supabase.gotrue.auth

class TeacherViewModel(
    private val repository: TeacherRepository = TeacherRepository()
) : ViewModel() {

    // For the list of teachers
    private val _uiState = MutableStateFlow<UiResult<List<Teacher>>>(UiResult.Idle)
    val uiState: StateFlow<UiResult<List<Teacher>>> = _uiState

    // For a single teacher detail
    private val _teacherDetailState = MutableStateFlow<UiResult<Teacher>>(UiResult.Idle)
    val teacherDetailState: StateFlow<UiResult<Teacher>> = _teacherDetailState

    // For delete operation status
    private val _deleteUiState = MutableStateFlow<UiResult<Unit>>(UiResult.Idle)
    val deleteUiState: StateFlow<UiResult<Unit>> = _deleteUiState

    // For current authenticated user ID
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    init {
        // Automatically load the list of teachers when the ViewModel is created
        fetchTeachers()
        loadCurrentUserId()
    }

    private fun loadCurrentUserId() {
        _currentUserId.value = SupabaseHolder.client.auth.currentUserOrNull()?.id
    }

    private fun fetchTeachers() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {
                val teachers = repository.getTeachers()
                _uiState.value = UiResult.Success(teachers)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun fetchTeacherById(id: Int) {
        viewModelScope.launch {
            _teacherDetailState.value = UiResult.Loading
            try {
                val teacher = repository.getTeacherById(id)
                if (teacher != null) {
                    _teacherDetailState.value = UiResult.Success(teacher)
                } else {
                    _teacherDetailState.value = UiResult.Error("Teacher not found")
                }
            } catch (e: Exception) {
                _teacherDetailState.value = UiResult.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun deleteTeacher(id: Int) {
        viewModelScope.launch {
            _deleteUiState.value = UiResult.Loading
            try {
                repository.deleteTeacher(id)
                _deleteUiState.value = UiResult.Success(Unit)
            } catch (e: Exception) {
                _deleteUiState.value = UiResult.Error(e.message ?: "Failed to delete teacher")
            }
        }
    }
}