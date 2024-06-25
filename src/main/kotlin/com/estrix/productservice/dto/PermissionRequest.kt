package com.estrix.productservice.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PermissionRequest(
    val domainObjectId: Long,
    val className: String,
    val sid: String,
    val permission: String,
    val isRole: Boolean
)