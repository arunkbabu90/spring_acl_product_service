package com.estrix.productservice.dto

data class AddProductResponse(
    val message: String,
    val status: Int,
    val success: Boolean,
    val username: String,
    val product: Any? = null
)
