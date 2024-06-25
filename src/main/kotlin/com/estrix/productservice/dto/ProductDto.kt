package com.estrix.productservice.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class ProductDto(
    @JsonProperty("product_id") var pid: String = "",
    val name: String,
    @JsonProperty("image_path") val imagePath: String,
    val price: Double
) {
    @JsonIgnore
    fun isBlank(): Boolean {
        return pid.isBlank() && name.isBlank() && imagePath.isBlank() && (price.isNaN() || price == 0.0)
    }

    @JsonIgnore
    fun isNotBlank(): Boolean {
        return !isBlank()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductDto

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
