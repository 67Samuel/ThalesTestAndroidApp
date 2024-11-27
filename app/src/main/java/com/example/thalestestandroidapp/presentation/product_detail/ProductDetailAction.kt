package com.example.thalestestandroidapp.presentation.product_detail

import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import java.io.File

sealed interface ProductDetailAction {

    data class OnConfirmProductUpdate(
        val name: String? = null,
        val image: File? = null,
        val description: String? = null,
        val type: Type? = null
    ): ProductDetailAction

    data class OnProductReceived(
        val product: Product
    ): ProductDetailAction
}