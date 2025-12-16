package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cobasupabase.data.repositories.AuthRepository
import com.example.cobasupabase.ui.common.UiResult
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<UiResult<Unit>>(UiResult.Idle)
    val authState: StateFlow<UiResult<Unit>> = _authState

    val isAuthenticated: StateFlow<Boolean> = repo.sessionStatus
        .map { status -> status is SessionStatus.Authenticated }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val currentUserEmail: StateFlow<String?> = repo.sessionStatus
        .map { status ->
            if (status is SessionStatus.Authenticated) {
                status.session.user?.email
            } else {
                null
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun register(email: String, password: String) {
        _authState.value = UiResult.Loading
        viewModelScope.launch {
            try {
                repo.register(email, password)
                _authState.value = UiResult.Success(Unit)
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
                _authState.value = UiResult.Success(Unit)
            } catch (e: Exception) {
                _authState.value = UiResult.Error(e.message ?: "Login gagal")
            }
        }
    }

     fun logout() {
         viewModelScope.launch {
             try {
                 repo.logout()
                 _authState.value = UiResult.Success(Unit)
             }
             catch (e: Exception) {
                 _authState.value = UiResult.Error(e.message ?: "Logout gagal")
             }
        }
    }
}