package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.NewsRepository
import com.example.cobasupabase.domain.model.News
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _uiState = MutableStateFlow<UiResult<List<News>>>(UiResult.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchNews()
    }
    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {
                val data = repository.getNews()
                _uiState.value = UiResult.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "Gagal memuat berita")
            }
        }
    }
}