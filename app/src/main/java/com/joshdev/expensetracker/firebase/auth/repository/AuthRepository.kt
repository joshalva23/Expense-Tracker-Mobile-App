package com.joshdev.expensetracker.firebase.auth.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.joshdev.expensetracker.firebase.auth.entity.AuthUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authUserFlow = MutableStateFlow<AuthUser?>(null)
    val authUserFlow: StateFlow<AuthUser?> = _authUserFlow.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { _authUserFlow.value = getUser() }
    }

    fun handleGoogleSignInResult(data: Intent?, onResult: (Boolean, String?) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        _authUserFlow.value = getUser()
                        onResult(true, null)
                    } else {
                        onResult(false, authTask.exception?.message)
                    }
                }
        } catch (e: ApiException) {
            onResult(false, e.message)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getUser(): AuthUser? {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        return firebaseUser?.let {
            AuthUser(
                uid = it.uid,
                name = it.displayName,
                email = it.email,
                profilePicUrl = it.photoUrl?.toString()
            )
        }
    }
}