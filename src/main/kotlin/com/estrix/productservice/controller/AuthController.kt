package com.estrix.productservice.controller

import com.estrix.productservice.domain.AuthToken
import com.estrix.productservice.dto.LoginResponse
import com.estrix.productservice.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthControllerImpl(
    private val authService: AuthService
) : AuthController {

    @GetMapping("/refresh/token")
    override fun refreshToken(@RequestParam("t") oldToken: String): ResponseEntity<Any> {
        val jwtToken: AuthToken = authService.refreshToken(oldToken)
        val roles: List<String> = jwtToken.authorities.map { it.authority }

        return ResponseEntity(
            LoginResponse(
                username = jwtToken.username,
                authorities = roles,
                bearerToken = jwtToken.token,
                createdAt = jwtToken.createdAt,
                expiresAt = jwtToken.expiresAt
            ), HttpStatus.CREATED
        )
    }
}

interface AuthController {
    fun refreshToken(oldToken: String): ResponseEntity<Any> = ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
}