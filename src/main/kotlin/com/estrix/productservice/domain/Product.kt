package com.estrix.productservice.domain

import jakarta.annotation.Nonnull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(unique = true)
    @Nonnull
    var pid: String = "",
    var name: String = "",
    var imagePath: String = "",
    var price: Double = 0.0
) {
    fun isBlank(): Boolean {
        return id == 0L && pid.isBlank() && name.isBlank() && imagePath.isBlank() && (price.isNaN() || price == 0.0)
    }

    fun isNotBlank(): Boolean {
        return !isBlank()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (pid != other.pid) return false
        if (name != other.name) return false
        if (imagePath != other.imagePath) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imagePath.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}
