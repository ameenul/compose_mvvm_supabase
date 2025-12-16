package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.NewsRepository
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditNewsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = NewsRepository()
    private val newsId: Int = checkNotNull(savedStateHandle["newsId"]).toString().toInt()

    private val _uiState = MutableStateFlow<UiResult<Boolean>>(UiResult.Idle)
    val uiState = _uiState.asStateFlow()

    // State Data Lama
    var titleInput = MutableStateFlow("")
        private set
    var contentInput = MutableStateFlow("")
        private set
    var authorInput = MutableStateFlow("")
        private set
    var imageUrlInput = MutableStateFlow("")
        private set

    // variable untuk menyimpan gambar baru (jika diganti EditBerita)
    private var newImageBytes: ByteArray? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val news = repository.getNewsById(newsId)
                titleInput.value = news.title
                contentInput.value = news.content
                authorInput.value = news.author ?: ""
                imageUrlInput.value = news.imageUrl ?: ""
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun onTitleChange(value: String) { titleInput.value = value }
    fun onContentChange(value: String) { contentInput.value = value }
    fun onAuthorChange(value: String) { authorInput.value = value }

    // fungsi untuk menerima file gambar dari UI
    fun onNewImageSelected(bytes: ByteArray) {
        newImageBytes = bytes
    }

    fun saveChanges() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {
                var finalImageUrl = imageUrlInput.value

                // Jika ada bytes baru, berarti user upload gambar baru
                if (newImageBytes != null) {
                    finalImageUrl = repository.uploadImage(newImageBytes!!)
                }

                repository.updateNews(
                    id = newsId,
                    title = titleInput.value,
                    content = contentInput.value,
                    author = authorInput.value,
                    imageUrl = finalImageUrl
                )
                _uiState.value = UiResult.Success(true)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "Gagal mengupdate berita")
            }
        }
    }
}