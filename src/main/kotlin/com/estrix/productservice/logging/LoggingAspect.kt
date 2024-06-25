package com.estrix.productservice.logging

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class LoggingAspect {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    /* Pointcut for the @Service annotation */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    fun servicePointcut() {}

    /* Pointcut for the @Controller annotation */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun controllerPointcut() {}

    @Before("servicePointcut() || controllerPointcut()")
    fun logBefore(joinPoint: JoinPoint) {
        log.info("Entering method - @Before : ${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}() " +
                "with arguments = ${joinPoint.args.size}")
    }

    @After("servicePointcut() || controllerPointcut()")
    fun logAfter(joinPoint: JoinPoint) {
        log.info("Exiting method - @After: ${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}() " +
                "with arguments = ${joinPoint.args.size}")
    }

    @AfterReturning(pointcut = "servicePointcut() || controllerPointcut()", returning = "result")
    fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
        log.info("Exiting method - @AfterReturning: ${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}() " +
                "with arguments = ${joinPoint.args.size} and result $result")
    }

    @AfterThrowing(pointcut = "servicePointcut() || controllerPointcut()", throwing = "error")
    fun logAfterThrowing(joinPoint: JoinPoint, error: Throwable) {
        log.error("Exception in method: ${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}() " +
                "with args = ${joinPoint.args.size} and error ${error.message ?: "error"}")
    }
}


