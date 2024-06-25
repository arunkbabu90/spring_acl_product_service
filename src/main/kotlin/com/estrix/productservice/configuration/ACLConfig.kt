package com.estrix.productservice.configuration

import com.estrix.productservice.utils.ADMIN
import com.estrix.productservice.utils.Role
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.AclAuthorizationStrategy
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl
import org.springframework.security.acls.domain.AuditLogger
import org.springframework.security.acls.domain.ConsoleAuditLogger
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy
import org.springframework.security.acls.domain.SpringCacheBasedAclCache
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.model.PermissionGrantingStrategy
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Configuration
@EnableMethodSecurity
class ACLConfig() {
    @Bean
    fun aclCache() = SpringCacheBasedAclCache(
        ConcurrentMapCache("acl_cache"),
        permissionGrantingStrategy(),
        aclAuthorizationStrategy()
    )

    @Bean
    fun auditLogger(): AuditLogger = ConsoleAuditLogger()

    @Bean
    fun dataSource() = DriverManagerDataSource().apply {
        setDriverClassName("org.postgresql.Driver")
        url = "jdbc:postgresql://localhost:5432/spring_acl_poc_db"
        username = "postgres"
        password = "root"
    }

    @Bean
    fun lookupStrategy() = BasicLookupStrategy(
        dataSource(),
        aclCache(),
        aclAuthorizationStrategy(),
        auditLogger()
    )

    @Bean
    fun aclService(): JdbcMutableAclService = JdbcMutableAclService(dataSource(), lookupStrategy(), aclCache()).apply {
        setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))")
        setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))")
    }

    @Bean
    fun aclAuthorizationStrategy(): AclAuthorizationStrategy = AclAuthorizationStrategyImpl(SimpleGrantedAuthority(Role.ADMIN))

    @Bean
    fun permissionGrantingStrategy(): PermissionGrantingStrategy = DefaultPermissionGrantingStrategy(auditLogger())

    @Bean
    fun aclSpringCacheManager() = SimpleCacheManager().apply {
        setCaches(listOf(ConcurrentMapCache("aclCache")))
    }

    @Bean
    fun aclPermissionEvaluator(aclService: JdbcMutableAclService): AclPermissionEvaluator = AclPermissionEvaluator(aclService)

    @Bean
    fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        val permissionEvaluator = AclPermissionEvaluator(aclService())
        expressionHandler.setPermissionEvaluator(permissionEvaluator)

        return expressionHandler
    }
}