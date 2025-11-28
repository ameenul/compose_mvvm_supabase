package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cobasupabase.domain.repositories.AuthRepository
import com.example.cobasupabase.ui.common.UiResult


class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<UiResult<Boolean>>(UiResult.Success(repo.currentSession() != null))
    val authState: StateFlow<UiResult<Boolean>> = _authState

    fun register(email: String, password: String) {
        _authState.value = UiResult.Loading
        viewModelScope.launch {
            try {
                repo.register(email, password)
                _authState.value = UiResult.Success(true)
            } catch (e: Exception) {
                _authState.value = UiResult.Error(e.message ?: "Register gagal")
            }
        }
    }

    fun login(email: String, password: String) {
        _authState.value = UiResult.Loading
        viewModelScope.launch {
            try {
                repo.login(email, password)
                _authState.value = UiResult.Success(true)
            } catch (e: Exception) {
                _authState.value = UiResult.Error(e.message ?: "Login gagal")
            }
        }
    }

     fun logout() {
         viewModelScope.launch {
             try {
                 repo.logout()
                 _authState.value = UiResult.Success(false)
             }
             catch (e: Exception) {
                 _authState.value = UiResult.Error(e.message ?: "Logout gagal")
         }}

    }


}