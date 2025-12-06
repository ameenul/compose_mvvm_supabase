package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.TeacherRepository
import com.example.cobasupabase.domain.model.Teacher
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import perfetto.protos.UiState

class TeacherViewModel : ViewModel() {
    private val repository = TeacherRepository()

    private val _uiState = MutableStateFlow<UiResult<List<Teacher>>>(UiResult.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchTeachers()
    }

    fun fetchTeachers() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {
                val data = repository.getTeachers()
                _uiState.value = UiResult.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "Gagal memuat guru")
            }
        }
    }
}