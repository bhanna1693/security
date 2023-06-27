package com.bhanna.security.auth

import com.bhanna.security.config.JwtService
import com.bhanna.security.user.Role
import com.bhanna.security.user.User
import com.bhanna.security.user.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {

    fun register(request: RegisterRequest): AuthenticationResponse {
        if (userRepository.findByEmail(request.email).isPresent) throw RuntimeException("User with email already exists. Please log in")

        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = Role.ADMIN
        )
        userRepository.save(user)
        return AuthenticationResponse(
            token = jwtService.generateToken(user)
        )
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        val user = userRepository.findByEmail(request.email)
            .orElseThrow()

        return AuthenticationResponse(
            token = jwtService.generateToken(user)
        )
    }
}