package com.example.thalestestandroidapp.presentation.product_list

import com.example.thalestestandroidapp.presentation.utils.UiText

sealed interface ProductListEvents {
    data class Error(val error: UiText): ProductListEvents
    data object NavigateToProductDetails: ProductListEvents
}