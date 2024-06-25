package com.estrix.productservice.domain

import org.springframework.security.core.GrantedAuthority

data class AuthToken(
    val username: String,
    val authorities: Collection<GrantedAuthority>,
    val token: String,
    val createdAt: Long,
    val expiresAt: Long,
)
