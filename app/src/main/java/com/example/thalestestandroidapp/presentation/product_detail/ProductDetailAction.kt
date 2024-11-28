package com.example.thalestestandroidapp.presentation.product_detail

import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import java.io.File

sealed interface ProductDetailAction {

    data class UpdateProduct(
        val id: Int, // If id is null, we are trying to create a new product
        val name: String? = null,
        val image: File? = null,
        val description: String? = null,
        val type: Type? = null
    ): ProductDetailAction

    data class CreateProduct(
        val name: String,
        val description: String,
        val type: Type
    ): ProductDetailAction

    /**
     * Null if a new Product is to be created
     */
    data class OnProductReceived(
        val product: Product? = null
    ): ProductDetailAction
}