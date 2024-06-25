package com.estrix.productservice.configuration

import com.estrix.productservice.authentication.JwtAuthenticationFilter
import com.estrix.productservice.authentication.JwtAuthorizationFilter
import com.estrix.productservice.utils.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import java.lang.Exception
import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
class WebSecurityConfig(private val jwtUtil: JwtUtil) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        return with(http) {
            authorizeHttpRequests {
                it.requestMatchers("/login").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/", "/product/all").permitAll()
                    .anyRequest().authenticated()
            }
            csrf { it.disable() }
            addFilter(JwtAuthenticationFilter(authenticationManager, jwtUtil))
            addFilter(JwtAuthorizationFilter(authenticationManager, jwtUtil))
        }.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val manager = InMemoryUserDetailsManager()
        manager.createUser(
            User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles(USER)
                .build()
        )
        manager.createUser(
            User.withDefaultPasswordEncoder()
                .username("guest")
                .password("password")
                .roles(GUEST)
                .build()
        )
        manager.createUser(
            User.withDefaultPasswordEncoder()
                .username("root")
                .password("rootadmin")
                .roles(ADMIN)
                .build()
        )
        manager.createUser(
            User.withDefaultPasswordEncoder()
                .username("john")
                .password("accessjohn")
                .roles(SELLER)
                .build()
        )
        manager.createUser(
            User.withDefaultPasswordEncoder()
                .username("mac")
                .password("accessmac")
                .roles(SELLER)
                .build()
        )
        return manager
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}