package com.estrix.productservice.controller

import com.estrix.productservice.dto.AddProductDto
import com.estrix.productservice.dto.AddProductResponse
import com.estrix.productservice.dto.ErrorResponse
import com.estrix.productservice.dto.ProductDto
import com.estrix.productservice.exception.NoUpdatesException
import com.estrix.productservice.mapper.ProductMapper
import com.estrix.productservice.service.ProductListService
import com.estrix.productservice.utils.Role
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductListControllerImpl(private val productListService: ProductListService) : ProductListController {
    private val mapper: ProductMapper = ProductMapper.INSTANCE

    @GetMapping("/seller")
    fun getSellerProducts(): List<ProductDto> {
        val products = productListService.getSellerProducts()
        return products.map { mapper.productToProductDto(it) }
    }

    @GetMapping("/all")
    override fun getProductList(@RequestParam("p") pageNo: Int): List<ProductDto> {
        val products = productListService.getProducts(pageNo)
        return products.map { mapper.productToProductDto(it) }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('${Role.SELLER}')")
    override fun addProduct(@RequestBody addProductDto: AddProductDto): ResponseEntity<Any> {
        val username = getUsername()
        val response: ResponseEntity<Any> = if (addProductDto.name.isNotBlank()) {
            val product = mapper.addProductDtoToProduct(addProductDto)
            val addedProduct = productListService.addProduct(product)

            if (addedProduct != null) {
                val addedProductDto = mapper.productToProductDto(addedProduct)
                ResponseEntity(
                    AddProductResponse(
                        message = "Product Added",
                        status = HttpStatus.CREATED.value(),
                        success = true,
                        username = username,
                        product = addedProductDto
                    ), HttpStatus.CREATED
                )
            } else {
                ResponseEntity(
                    ErrorResponse(
                    statusMessage = "Error! Product not added",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
                ), HttpStatus.INTERNAL_SERVER_ERROR)
            }
        } else {
            ResponseEntity(
                AddProductResponse(
                message = "Bad Request. Product Details are invalid",
                status = HttpStatus.BAD_REQUEST.value(),
                success = false,
                username = username,
                addProductDto
            ), HttpStatus.BAD_REQUEST)
        }

        return response
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('${Role.SELLER}')")
    override fun editProduct(@RequestBody updatedProductDto: ProductDto): ResponseEntity<Any> {
        val product = productListService.editProduct(updatedProductDto)
            ?: throw NoUpdatesException("Unknown Error! Product is not updated")

        val productDto = mapper.productToProductDto(product)
        return ResponseEntity(productDto, HttpStatus.OK)
    }

    private fun getUsername(): String {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.principal.toString()
    }
}

interface ProductListController {
    fun getProductList(pageNo: Int) = listOf<ProductDto>()
    fun addProduct(addProductDto: AddProductDto): ResponseEntity<Any> = ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    fun editProduct(updatedProductDto: ProductDto): ResponseEntity<Any> = ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
}