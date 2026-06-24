package com.neskvik.pomotask.auth

data class AuthState(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val email: String = "",
    val token: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
