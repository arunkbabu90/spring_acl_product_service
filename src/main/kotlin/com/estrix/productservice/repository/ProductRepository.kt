package com.estrix.productservice.repository

import com.estrix.productservice.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.security.access.prepost.PostAuthorize
import java.util.*

interface ProductRepository : JpaRepository<Product, String> {
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    override fun findById(pid: String): Optional<Product>

    fun findByPid(pid: String): Product?

    fun findTopByOrderByPidDesc(): Product?
}

