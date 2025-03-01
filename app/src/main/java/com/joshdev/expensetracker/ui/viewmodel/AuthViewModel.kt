package com.joshdev.expensetracker.ui.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joshdev.expensetracker.firebase.auth.entity.AuthUser
import com.joshdev.expensetracker.firebase.auth.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val user: StateFlow<AuthUser?> = authRepository.authUserFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun handleGoogleSignInResult(data: Intent?, onResult: (Boolean, String?) -> Unit) {
        authRepository.handleGoogleSignInResult(data, onResult)
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }

}

class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
