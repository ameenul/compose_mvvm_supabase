package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.dto.ReviewDto
import com.example.cobasupabase.data.repositories.ReviewRepository
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val repo: ReviewRepository = ReviewRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<UiResult<List<ReviewDto>>>(UiResult.Idle)
    val state = _state.asStateFlow()

    private val _uploadState = MutableStateFlow<UiResult<Boolean>>(UiResult.Idle)
    val uploadState = _uploadState.asStateFlow()

    val userNameInput = MutableStateFlow("")
    val ratingInput = MutableStateFlow(5)
    val commentInput = MutableStateFlow("")
    val imageBytesInput = MutableStateFlow<ByteArray?>(null)

    fun loadReviews(teacherId: Int) {
        viewModelScope.launch {
            _state.value = UiResult.Loading
            try {
                val data = repo.getReviews(teacherId)
                _state.value = UiResult.Success(data)
            } catch (e: Exception) {
                _state.value = UiResult.Error(e.message ?: "Gagal memuat data")
            }
        }
    }

    fun loadAllReviews() {
        viewModelScope.launch {
            _state.value = UiResult.Loading
            try {
                val data = repo.getAllReviews()
                _state.value = UiResult.Success(data)
            } catch (e: Exception) {
                _state.value = UiResult.Error(e.message ?: "Gagal memuat semua data review")
            }
        }
    }

    fun addReview(teacherId: Int, name: String, rating: Int, comment: String, image: ByteArray?) {
        viewModelScope.launch {
            _uploadState.value = UiResult.Loading
            try {
                repo.createReview(teacherId, name, rating, comment, image)
                _uploadState.value = UiResult.Success(true)
                loadReviews(teacherId)
                resetForm()
            } catch (e: Exception) {
                _uploadState.value = UiResult.Error(e.message ?: "Gagal mengirim review")
            }
        }
    }

    fun resetUploadState() {
        _uploadState.value = UiResult.Idle
    }

    private fun resetForm() {
        userNameInput.value = ""
        ratingInput.value = 5
        commentInput.value = ""
        imageBytesInput.value = null
    }
}