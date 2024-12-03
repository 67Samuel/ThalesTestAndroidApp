package com.example.thalestestandroidapp.presentation.product_list

import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.SortOption

sealed interface ProductListAction {
    data class OnProductClick(val product: Product): ProductListAction
    data class SortProductsBy(val sortBy: SortOption): ProductListAction
    data object RefreshProducts : ProductListAction
}