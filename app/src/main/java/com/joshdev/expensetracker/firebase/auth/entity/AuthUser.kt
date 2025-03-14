package com.joshdev.expensetracker.firebase.auth.entity

data class AuthUser(
    val uid: String,
    val name: String?,
    val email: String?,
    val profilePicUrl: String?
)