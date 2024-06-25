package com.estrix.productservice.controller

import com.estrix.productservice.domain.Product
import com.estrix.productservice.dto.ErrorResponse
import com.estrix.productservice.dto.PermissionRequest
import com.estrix.productservice.dto.SimpleResponse
import com.estrix.productservice.service.AccessControlService
import com.estrix.productservice.utils.Role
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.acls.model.NotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/acl/permission")
@PreAuthorize("hasRole('${Role.ADMIN}')")
class AccessControlController(private val acs: AccessControlService) {

    @PostMapping
    fun addUserOrRole(@RequestBody permissionRequest: PermissionRequest): ResponseEntity<*> {
        try {
            acs.addUserOrRole(permissionRequest)
        } catch (e: NotFoundException) {
            val errorResponse = ErrorResponse(
                statusMessage = "Failed to Add Permission because ACL related object is not found",
                statusCode = HttpStatus.NOT_FOUND.value()
            )
            return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
        }

        return ResponseEntity(
            SimpleResponse(
            "${permissionRequest.permission.uppercase()} permission added for ${permissionRequest.sid}",
            HttpStatus.CREATED.value(),
            true
        ), HttpStatus.CREATED)
    }

    @DeleteMapping("/user")
    fun deletePermissionForUser(
        @RequestParam domainObjectId: Long,
        @RequestParam className: String,
        @RequestParam username: String,
        @RequestParam permission: String
    ): ResponseEntity<*> {
        acs.deletePermissionForUser(domainObjectId, className, username, permission)

        return ResponseEntity(
            SimpleResponse(
            "${permission.uppercase()} permission deleted for $username",
            HttpStatus.OK.value(),
            true
        ), HttpStatus.OK)
    }

    @DeleteMapping
    fun revokePermission(
        @RequestParam("pid") productId: String,
        @RequestParam("u") username: String,
        @RequestParam("p") permission: String
    ): ResponseEntity<*> {
        val domainObject = acs.revokePermission(productId, username, permission)

        if (domainObject !is Product)
            throw Exception()

        return ResponseEntity(
            SimpleResponse(
                "${permission.uppercase()} permission revoked for $username for ${domainObject.name} with id ${domainObject.pid}",
                HttpStatus.OK.value(),
                true
            ), HttpStatus.OK
        )
    }
}