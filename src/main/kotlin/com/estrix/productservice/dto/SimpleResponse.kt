package com.estrix.productservice.dto

data class SimpleResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean
)
