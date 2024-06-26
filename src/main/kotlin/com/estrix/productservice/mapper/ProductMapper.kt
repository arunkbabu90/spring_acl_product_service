package com.estrix.productservice.mapper

import com.estrix.productservice.domain.Product
import com.estrix.productservice.dto.AddProductDto
import com.estrix.productservice.dto.ProductDto
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface   ProductMapper {
    companion object {
        val INSTANCE: ProductMapper = Mappers.getMapper(ProductMapper::class.java)
    }

    fun productToProductDto(product: Product): ProductDto
    fun productDtoToProduct(productDto: ProductDto): Product

    fun addProductDtoToProduct(addProductDto: AddProductDto): Product
    fun productToAddProductDto(product: Product): AddProductDto
}