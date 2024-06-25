package com.estrix.productservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class ProductserviceApplication

fun main(args: Array<String>) {
	runApplication<ProductserviceApplication>(*args)
}
