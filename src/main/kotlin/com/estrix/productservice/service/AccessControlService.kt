package com.estrix.productservice.service

import com.estrix.productservice.domain.Product
import com.estrix.productservice.exception.ResourceNotFoundException
import com.estrix.productservice.repository.ProductRepository
import com.estrix.productservice.utils.ADMINISTRATION
import com.estrix.productservice.utils.DELETE
import com.estrix.productservice.utils.READ
import com.estrix.productservice.utils.WRITE
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.model.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccessControlServiceImpl(
    private val aclService: JdbcMutableAclService,
    private val permissionEvaluator: AclPermissionEvaluator,
    private val productRepository: ProductRepository
): AccessControlService {

    @Transactional
    override fun addPermissionForObject(domainObject: Any, permission: Permission) {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.principal.toString()

        if (domainObject is Product) {
            val objectIdentity: ObjectIdentity = ObjectIdentityImpl(domainObject)

            val acl: MutableAcl = try {
                aclService.readAclById(objectIdentity) as MutableAcl
            } catch (e: Exception) {
                aclService.createAcl(objectIdentity)
            }

            val sid: Sid = PrincipalSid(username)
            acl.insertAce(acl.entries.size, permission, sid, true)
            aclService.updateAcl(acl)
        } else {
            throw IllegalArgumentException("The domain object is invalid")
        }
    }

    @Transactional
    override fun addPermissionForUser(
        domainObjectId: Long,
        className: String,
        username: String,
        permission: String
    ) {
        val domainObject = Class.forName(className).getDeclaredConstructor().newInstance() as Product

        val basePermission = when(permission.uppercase()) {
            READ -> BasePermission.READ
            WRITE -> BasePermission.WRITE
            else -> throw IllegalArgumentException("Invalid permission: $permission")
        } as BasePermission

        val objectIdentity: ObjectIdentity = ObjectIdentityImpl(domainObject.javaClass, domainObjectId)

        val acl: MutableAcl = try {
            aclService.readAclById(objectIdentity) as MutableAcl
        } catch (e: NotFoundException) {
            aclService.createAcl(objectIdentity)
        }

        val sid: Sid = PrincipalSid(username)
        acl.insertAce(acl.entries.size, basePermission, sid, true)
        aclService.updateAcl(acl)
    }

    @Transactional
    override fun deletePermissionForUser(
        domainObjectId: Long,
        className: String,
        username: String,
        permission: String
    ) {
        val objectIdentity: ObjectIdentity = ObjectIdentityImpl(className, domainObjectId)
        val basePermission = permissionToBasePermission(permission)

        val acl: MutableAcl = try {
            aclService.readAclById(objectIdentity) as MutableAcl
        } catch (e: NotFoundException) {
            throw NotFoundException("ACL object not found for the object: $domainObjectId of class: $className")
        }

        val sid: Sid = PrincipalSid(username)
        val entries: MutableList<AccessControlEntry> = acl.entries

        val aceIndex = entries.indexOfFirst { it.sid == sid && it.permission == basePermission }
        val isEntryPresent = aceIndex >= 0

        if (isEntryPresent) {
            acl.deleteAce(aceIndex)
            aclService.updateAcl(acl)
        } else {
            throw NotFoundException("ACE not found for $username with permission: $basePermission")
        }
    }

    @Transactional
    override fun revokePermission(productId: String, username: String, permission: String): Any? {
        val product = productRepository.findByPid(productId)
            ?: throw ResourceNotFoundException("Product with id $productId not found")

        val objectIdentity: ObjectIdentity = ObjectIdentityImpl(product)
        val basePermission = permissionToBasePermission(permission)

        val acl: MutableAcl = try {
            aclService.readAclById(objectIdentity) as MutableAcl
        } catch (e: NotFoundException) {
            throw NotFoundException("ACL object not found for the given entry")
        }

        val sid: Sid = PrincipalSid(username)
        val entries = acl.entries

        val aceIndex = entries.indexOfFirst {
            it.sid == sid && it.permission == basePermission
        }

        val isEntryPresent = aceIndex >= 0
        if (isEntryPresent) {
            acl.deleteAce(aceIndex)
            aclService.updateAcl(acl)
        } else {
            throw NotFoundException("ACE not found for $username with permission: $permission")
        }

        return product
    }

    override fun hasPermission(product: Product, permission: String): Boolean {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return permissionEvaluator.hasPermission(authentication, product, permission)
    }

    private fun permissionToBasePermission(permission: String): BasePermission =
        when (permission.uppercase()) {
            READ -> BasePermission.READ
            WRITE -> BasePermission.WRITE
            DELETE -> BasePermission.DELETE
            ADMINISTRATION -> BasePermission.ADMINISTRATION
            else -> throw IllegalArgumentException("Invalid permission: $permission")
        } as BasePermission
}

interface AccessControlService {
    fun addPermissionForObject(domainObject: Any, permission: Permission) { }
    fun addPermissionForUser(domainObjectId: Long, className: String, username: String, permission: String) { }
    fun deletePermissionForUser(domainObjectId: Long, className: String, username: String, permission: String)
    fun revokePermission(productId: String, username: String, permission: String): Any? { return null }
    fun hasPermission(product: Product, permission: String) = false
}