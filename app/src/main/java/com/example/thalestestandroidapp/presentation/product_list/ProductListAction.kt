package com.example.thalestestandroidapp.presentation.product_list

import com.example.thalestestandroidapp.domain.models.Product

sealed interface ProductListAction {
    data class OnProductClick(val product: Product): ProductListAction
}