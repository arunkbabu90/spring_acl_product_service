package com.estrix.productservice.service

import com.estrix.productservice.domain.Product
import com.estrix.productservice.dto.ProductDto
import com.estrix.productservice.exception.*
import com.estrix.productservice.mapper.ProductMapper
import com.estrix.productservice.repository.ProductRepository
import com.estrix.productservice.utils.Role
import com.estrix.productservice.utils.WRITE
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class ProductListServiceImpl(
    private val productRepository: ProductRepository,
    private val acs: AccessControlService,
) : ProductListService {

    private val mapper = ProductMapper.INSTANCE

    @PostFilter("hasPermission(filterObject, 'WRITE')")
    override fun getSellerProducts(): List<Product> = productRepository.findAll()

    @Transactional
    override fun addProduct(product: Product): Product? {
        val isSeller = getUserRole().contains(Role.SELLER)

        return if (isSeller) {
            val isProductExists = productRepository.findByPid(product.pid) != null
            if (isProductExists) {
                throw ResourceExistsException("Product Id already taken")
            }

            // Generate custom product ID
            val generatedPid = generateProductId() ?: throw AddProductException("Failed to Add Product")

            product.apply {
                pid = generatedPid
            }

            val savedProduct = productRepository.save(product)
            acs.addPermissionForObject(savedProduct, BasePermission.WRITE)
            savedProduct
        } else {
            null
        }
    }

    private fun getUserRole(): List<String> {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.authorities.map { it.authority }
    }

    override fun getProducts(pageNo: Int): List<Product> {
        return when (pageNo) {
            1 -> {
                productRepository.findAll()
            }
            else -> throw PageNotFoundException("Page Not Found")
        }
    }

    override fun getProduct(productId: String): Product {
        val product = productRepository.findByPid(productId)
        if (product != null) {
            return product
        } else {
            throw ResourceNotFoundException("Product Not Found")
        }
    }

    @Transactional
    override fun editProduct(updatedProductDto: ProductDto): Product? {
        if (updatedProductDto.isBlank()) {
            throw BadRequestException("No Data to perform Update")
        }

        val updatedProduct: Product = mapper.productDtoToProduct(updatedProductDto)
        val product = getProduct(updatedProductDto.pid)

        // Check if the seller has permission to EDIT the product
        if (!acs.hasPermission(product, WRITE)) {
            throw AccessDeniedException("Access Denied! You don't have permission to edit this product")
        }

        if (updatedProduct == product) {
            throw NoUpdatesException("There are no updates to perform")
        }

        updatedProduct.apply {
            id = product.id
        }

        return if (updatedProduct.isNotBlank()) { productRepository.save(updatedProduct) } else { null }
    }

    private fun generateProductId(): String? {
        val latestProduct = productRepository.findTopByOrderByPidDesc()
        val latestPid = latestProduct?.pid

        return if (latestPid != null) {
            val numericPart = latestPid.substringAfter("PRD_").substringBefore("_RT").toLongOrNull() ?: 100000000
            val newNumericPart = numericPart + 1
            "PRD_${newNumericPart}_RT"
        } else {
            null
        }
    }
}

interface ProductListService {
    fun addProduct(product: Product): Product? = null
    fun getProducts(pageNo: Int) = listOf<Product>()
    fun getSellerProducts() = listOf<Product>()
    fun getProduct(productId: String): Product? = null
    fun editProduct(updatedProductDto: ProductDto): Product? = null
}