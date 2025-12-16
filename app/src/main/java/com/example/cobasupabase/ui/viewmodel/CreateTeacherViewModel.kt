package com.example.cobasupabase.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.dto.TeacherInsert
import com.example.cobasupabase.data.repositories.TeacherRepository
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.cobasupabase.data.remote.SupabaseHolder
import io.github.jan.supabase.gotrue.auth

class CreateTeacherViewModel(
    private val repository: TeacherRepository = TeacherRepository()
) : ViewModel() {

    // Form fields
    var name by mutableStateOf("")
    var subject by mutableStateOf("")
    var educationHistory by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var description by mutableStateOf("")
    var certifications by mutableStateOf("")
    var experience by mutableStateOf("")
    var linkedinUrl by mutableStateOf("")
    var imageUrl by mutableStateOf("")
    var educationLevel by mutableStateOf("")
    var price by mutableStateOf("")
    var imageBytes = MutableStateFlow<ByteArray?>(null)
        private set

    // UI state for the creation process
    private val _uiState = MutableStateFlow<UiResult<Unit>?>(null)
    val uiState: StateFlow<UiResult<Unit>?> = _uiState

    fun onImageSelected(bytes: ByteArray) {
        imageBytes.value = bytes
    }

    fun createTeacher() {
        if (name.isBlank() || subject.isBlank() || educationHistory.isBlank() || phoneNumber.isBlank() || description.isBlank()) {
            _uiState.value = UiResult.Error("Please fill all required fields.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            try {
                val currentUser = SupabaseHolder.client.auth.currentUserOrNull()
                    ?: throw Exception("User not logged in. Cannot create teacher profile.")

                var finalImageUrl = imageUrl
                val currentImageBytes = imageBytes.value
                if (currentImageBytes != null) {
                    finalImageUrl = repository.uploadImage(currentImageBytes, currentUser.id)
                }

                val teacherData = TeacherInsert(
                    userId = currentUser.id,
                    name = name,
                    subject = subject,
                    educationHistory = educationHistory,
                    phoneNumber = phoneNumber,
                    description = description,
                    certifications = certifications.takeIf { it.isNotBlank() },
                    experience = experience.takeIf { it.isNotBlank() },
                    linkedinUrl = linkedinUrl.takeIf { it.isNotBlank() },
                    imageUrl = finalImageUrl.takeIf { it.isNotBlank() },
                    educationLevel = educationLevel.takeIf { it.isNotBlank() },
                    price = price.takeIf { it.isNotBlank() }
                )
                repository.createTeacher(teacherData)
                _uiState.value = UiResult.Success(Unit)
            } catch (e: Exception) {
                _uiState.value = UiResult.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}