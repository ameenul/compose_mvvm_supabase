package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.NewsRepository
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddNewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    // State untuk status Loading/Success/Error
    private val _uiState = MutableStateFlow<UiResult<Boolean>>(UiResult.Idle)
    val uiState = _uiState.asStateFlow()

    // State untuk Form Input
    var titleInput = MutableStateFlow("")
        private set
    var contentInput = MutableStateFlow("")
        private set
    var authorInput = MutableStateFlow("")
        private set
    var imageUrlInput = MutableStateFlow<ByteArray?>(null)
        private set

    // Setter untuk memperbarui nilai saat user mengetik
    fun onTitleChange(value: String) { titleInput.value = value }
    fun onContentChange(value: String) { contentInput.value = value }
    fun onAuthorChange(value: String) { authorInput.value = value }
    fun onImageSelected(bytes: ByteArray) {
        imageUrlInput.value = bytes
    }

    fun createNews() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {

                if (titleInput.value.isBlank() || contentInput.value.isBlank()) {
                    throw Exception("Judul dan Konten wajib diisi!")
                }

                var finalImageUrl = "https://via.placeholder.com/400"

                val currentImageBytes = imageUrlInput.value
                if (currentImageBytes != null) {
                    finalImageUrl = repository.uploadImage(currentImageBytes)
                }

                repository.createNews(
                    title = titleInput.value,
                    content = contentInput.value,
                    author = authorInput.value,
                    imageUrl = finalImageUrl
                )

                _uiState.value = UiResult.Success(true)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "Gagal membuat berita")
            }
        }
    }
}