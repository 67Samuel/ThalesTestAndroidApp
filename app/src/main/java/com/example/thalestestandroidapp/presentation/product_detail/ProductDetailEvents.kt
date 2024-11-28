package com.example.thalestestandroidapp.presentation.product_detail

import com.example.thalestestandroidapp.presentation.utils.UiText

sealed interface ProductDetailEvents {
    data class Error(val error: UiText): ProductDetailEvents
    data class ProductUpdated(val message: UiText): ProductDetailEvents
}