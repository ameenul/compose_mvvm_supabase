package com.example.cobasupabase.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.dto.TeacherInsert
import com.example.cobasupabase.data.repositories.TeacherRepository
import com.example.cobasupabase.domain.model.Teacher
import com.example.cobasupabase.ui.common.UiResult
import io.github.jan.supabase.gotrue.auth
import com.example.cobasupabase.data.remote.SupabaseHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditTeacherViewModel(
    private val repository: TeacherRepository = TeacherRepository()
) : ViewModel() {

    var teacherId: Int? = null

    // State for fetching the initial teacher data
    private val _teacherDataState = MutableStateFlow<UiResult<Teacher>?>(UiResult.Idle)
    val teacherDataState: StateFlow<UiResult<Teacher>?> = _teacherDataState

    // Form fields
    var name by mutableStateOf("")
    var subject by mutableStateOf("")
    var educationHistory by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var description by mutableStateOf("")
    var certifications by mutableStateOf("")
    var experience by mutableStateOf("")
    var linkedinUrl by mutableStateOf("")
    var imageUrl by mutableStateOf("") // This will hold the current URL or the new uploaded URL
    val editableEducationTags = mutableStateListOf<String>() // Changed to mutableStateListOf
    var price by mutableStateOf("")
    var imageBytes = MutableStateFlow<ByteArray?>(null)
        private set

    // UI state for the update process
    private val _updateUiState = MutableStateFlow<UiResult<Unit>?>(null)
    val updateUiState: StateFlow<UiResult<Unit>?> = _updateUiState

    fun loadTeacher(id: Int) {
        teacherId = id
        viewModelScope.launch {
            _teacherDataState.value = UiResult.Loading
            try {
                val teacher = repository.getTeacherById(id)
                if (teacher != null) {
                    name = teacher.name
                    subject = teacher.subject
                    educationHistory = teacher.educationHistory
                    phoneNumber = teacher.phoneNumber
                    description = teacher.description
                    certifications = teacher.certifications ?: ""
                    experience = teacher.experience ?: ""
                    linkedinUrl = teacher.linkedinUrl ?: ""
                    imageUrl = teacher.imageUrl ?: ""
                    editableEducationTags.clear()
                    editableEducationTags.addAll(teacher.educationTags)
                    price = teacher.price ?: ""
                    _teacherDataState.value = UiResult.Success(teacher)
                } else {
                    _teacherDataState.value = UiResult.Error("Teacher not found")
                }
            } catch (e: Exception) {
                _teacherDataState.value = UiResult.Error(e.message ?: "Failed to load teacher data")
            }
        }
    }

    fun onImageSelected(bytes: ByteArray) {
        imageBytes.value = bytes
    }

    fun updateTeacher() {
        val currentTeacherId = teacherId ?: run {
            _updateUiState.value = UiResult.Error("Teacher ID is missing.")
            return
        }

        if (name.isBlank() || subject.isBlank() || educationHistory.isBlank() || phoneNumber.isBlank() || description.isBlank()) {
            _updateUiState.value = UiResult.Error("Please fill all required fields.")
            return
        }

        viewModelScope.launch {
            _updateUiState.value = UiResult.Loading
            try {
                val currentUser = SupabaseHolder.client.auth.currentUserOrNull()
                    ?: throw Exception("User not logged in. Cannot update teacher profile.")

                var finalImageUrl = imageUrl // Start with current image URL
                val currentImageBytes = imageBytes.value
                if (currentImageBytes != null) {
                    // If new image bytes are selected, upload it
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
                    educationLevel = editableEducationTags.joinToString().takeIf { it.isNotBlank() }, // Join tags for DTO
                    price = price.takeIf { it.isNotBlank() }
                )
                repository.updateTeacher(currentTeacherId, teacherData)
                _updateUiState.value = UiResult.Success(Unit)
            } catch (e: Exception) {
                _updateUiState.value = UiResult.Error(e.message ?: "An unknown error occurred during update")
            }
        }
    }
}