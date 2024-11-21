package com.example.thalestestandroidapp.presentation.product_list

import com.example.thalestestandroidapp.domain.util.NetworkError

sealed interface ProductListEvents {
    data class Error(val error: NetworkError): ProductListEvents
    object NavigateToProductDetails: ProductListEvents
}