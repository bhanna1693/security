package com.bhanna.security.auth

import com.bhanna.security.config.JwtService
import com.bhanna.security.user.Role
import com.bhanna.security.user.User
import com.bhanna.security.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationServiceTest {

    private final val userRepository: UserRepository = mockk()
    private final val passwordEncoder: PasswordEncoder = mockk()
    private final val jwtService: JwtService = mockk()
    private final val authenticationManager: AuthenticationManager = mockk()

    lateinit var authenticationService: AuthenticationService

    @BeforeEach
    fun setUp() {
        authenticationService = AuthenticationService(
            userRepository, passwordEncoder, jwtService, authenticationManager
        )
    }

    @Test
    fun registerAndSuccess() {
        val request = RegisterRequest(
            firstName = "Brian",
            lastName = "Hanna",
            email = "bhanna@test.com",
            password = "password"
        )
        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = request.password,
            role = Role.ADMIN,
        )
        val authToken = "authToken"

        every { userRepository.findByEmail(any()) } returns Optional.empty()
        every { userRepository.save(any()) } returns user
        every { passwordEncoder.encode(any()) } returns "encodedPassword"
        every { jwtService.generateToken(any()) } returns authToken

        val response = AuthenticationResponse(
            token = authToken
        )

        assertEquals(response, authenticationService.register(request))
    }

    @Test
    fun registerAndEmailAlreadyExists() {
        val request = RegisterRequest(
            firstName = "Brian",
            lastName = "Hanna",
            email = "bhanna@test.com",
            password = "password"
        )
        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = request.password,
            role = Role.ADMIN,
        )

        every { userRepository.findByEmail(any()) } returns Optional.of(user)

        assertThrows<RuntimeException> { authenticationService.register(request) }
    }

    @Test
    fun authenticate() {
        val request = AuthenticationRequest(
            email = "bhanna@test.com",
            password = "password"
        )
        val user = User(
            firstName = "Brian",
            lastName = "Hanna",
            email = request.email,
            password = request.password,
            role = Role.ADMIN,
        )
        val authToken = "authToken"

        every { authenticationManager.authenticate(any()) } returns null
        every { userRepository.findByEmail(any()) } returns Optional.of(user)
        every { jwtService.generateToken(any()) } returns authToken

        val response = AuthenticationResponse(
            token = authToken
        )

        assertEquals(response, authenticationService.authenticate(request))
    }

    @Test
    fun authenticateAndBadCredentials() {
        val request = AuthenticationRequest(
            email = "bhanna@test.com",
            password = "password"
        )

        every { authenticationManager.authenticate(any()) } throws BadCredentialsException("")

        assertThrows<BadCredentialsException> { authenticationService.authenticate(request) }
    }

    @Test
    fun authenticateAndUserNotFound() {
        val request = AuthenticationRequest(
            email = "bhanna@test.com",
            password = "password"
        )

        every { authenticationManager.authenticate(any()) } returns null
        every { userRepository.findByEmail(any()) } returns Optional.empty()


        assertThrows<NoSuchElementException> { authenticationService.authenticate(request) }
    }
}