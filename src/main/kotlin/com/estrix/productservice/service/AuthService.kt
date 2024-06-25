package com.estrix.productservice.service

import com.estrix.productservice.domain.AuthToken
import com.estrix.productservice.utils.JwtUtil
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : AuthService {

    override fun refreshToken(oldToken: String): AuthToken {
        val username: String = jwtUtil.extractUsernameFromToken(oldToken)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
        return jwtUtil.generateToken(userDetails)
    }
}

interface AuthService {
    fun refreshToken(oldToken: String): AuthToken
}