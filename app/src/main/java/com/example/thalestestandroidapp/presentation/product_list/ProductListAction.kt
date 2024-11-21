package com.example.thalestestandroidapp.presentation.product_list

sealed interface ProductListAction {
    data class OnProductClick(val productId: Int): ProductListAction
}