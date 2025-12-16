package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.NewsRepository
import com.example.cobasupabase.domain.model.News
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.cobasupabase.data.remote.SupabaseHolder
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.StateFlow

class NewsDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = NewsRepository()

    private val newsId: Int = checkNotNull(savedStateHandle["newsId"]).toString().toInt()

    private val _uiState = MutableStateFlow<UiResult<News>>(UiResult.Loading)
    val uiState = _uiState.asStateFlow()

    //khusus delete berita
    private val _deleteUiState = MutableStateFlow<UiResult<Boolean>>(UiResult.Idle)
    val deleteUiState = _deleteUiState.asStateFlow()

    // For current authenticated user ID
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    init {
        fetchNewsDetail()
        loadCurrentUserId()
    }

    private fun loadCurrentUserId() {
        _currentUserId.value = SupabaseHolder.client.auth.currentUserOrNull()?.id
    }

    fun fetchNewsDetail() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {
                val data = repository.getNewsById(newsId)
                _uiState.value = UiResult.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "Gagal memuat detail berita")
            }
        }
    }

    fun deleteNews() {
        viewModelScope.launch {
            _deleteUiState.value = UiResult.Loading
            try {
                repository.deleteNews(newsId)
                _deleteUiState.value = UiResult.Success(true) // Berhasil hapus
            } catch (e: Exception) {
                _deleteUiState.value = UiResult.Error(e.message ?: "Gagal menghapus berita")
            }
        }
    }

    // Fungsi reset state
    fun resetDeleteState() {
        _deleteUiState.value = UiResult.Idle
    }
}