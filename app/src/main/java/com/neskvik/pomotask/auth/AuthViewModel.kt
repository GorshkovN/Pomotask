package com.neskvik.pomotask.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neskvik.pomotask.network.ApiResult
import com.neskvik.pomotask.network.AuthApi
import com.neskvik.pomotask.setting.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val token = dataStoreManager.getAuthToken().first()
            if (!token.isNullOrEmpty()) {
                _state.update {
                    it.copy(
                        isLoggedIn = true,
                        token = token,
                        username = dataStoreManager.getAuthUsername().first() ?: "",
                        email = dataStoreManager.getAuthEmail().first() ?: ""
                    )
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Заполните все поля") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = AuthApi.login(email.trim(), password)) {
                is ApiResult.Success -> saveAndApply(result.data.token, result.data.username, result.data.email)
                is ApiResult.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Заполните все поля") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = AuthApi.register(username.trim(), email.trim(), password)) {
                is ApiResult.Success -> saveAndApply(result.data.token, result.data.username, result.data.email)
                is ApiResult.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearAuthData()
            _state.update { AuthState() }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private suspend fun saveAndApply(token: String, username: String, email: String) {
        dataStoreManager.saveAuthData(token, username, email)
        _state.update {
            it.copy(
                isLoading = false,
                isLoggedIn = true,
                token = token,
                username = username,
                email = email
            )
        }
    }
}
