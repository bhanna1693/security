package com.bhanna.security.auth

data class AuthenticationRequest(
        val email: String,
        val password: String
)
