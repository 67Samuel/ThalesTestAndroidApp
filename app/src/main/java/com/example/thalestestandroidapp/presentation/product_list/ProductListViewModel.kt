package com.example.thalestestandroidapp.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _productList = MutableStateFlow(emptyList<Product>())
    val productList = _productList.asStateFlow()

    private val _productListEvents = Channel<ProductListEvents>()
    val productListEvents = _productListEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadProducts()
        }
    }

    fun onAction(action: ProductListAction) {
        when (action) {
            is ProductListAction.OnProductClick -> {
                viewModelScope.launch {
                    _productListEvents.send(ProductListEvents.NavigateToProductDetails)
                }
            }
        }
    }

    private suspend fun loadProducts() {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            delay(2000)

            _productList.update { listOf(
                Product(
                    id = 5080,
                    name = "Blanca Bradley",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=tincidunt",
                    description = "deserunt"
                ),
                Product(
                    id = 3881,
                    name = "Cleveland Baird",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=veniam",
                    description = "eius"
                ),
                Product(
                    id = 2081,
                    name = "Cleo Craft",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=postea",
                    description = "propriae"
                ),
                Product(
                    id = 6185,
                    name = "Marissa Bartlett",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=harum",
                    description = "pri"
                ),
                Product(
                    id = 5080,
                    name = "Blanca Bradley",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=tincidunt",
                    description = "deserunt"
                ),
                Product(
                    id = 3881,
                    name = "Cleveland Baird",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=veniam",
                    description = "eius"
                ),
                Product(
                    id = 2081,
                    name = "Cleo Craft",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=postea",
                    description = "propriae"
                ),
                Product(
                    id = 6185,
                    name = "Marissa Bartlett",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=harum",
                    description = "pri"
                ),
                Product(
                    id = 5080,
                    name = "Blanca Bradley",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=tincidunt",
                    description = "deserunt"
                ),
                Product(
                    id = 3881,
                    name = "Cleveland Baird",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=veniam",
                    description = "eius"
                ),
                Product(
                    id = 2081,
                    name = "Cleo Craft",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=postea",
                    description = "propriae"
                ),
                Product(
                    id = 6185,
                    name = "Marissa Bartlett",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=harum",
                    description = "pri"
                ),
                Product(
                    id = 5080,
                    name = "Blanca Bradley",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=tincidunt",
                    description = "deserunt"
                ),
                Product(
                    id = 3881,
                    name = "Cleveland Baird",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=veniam",
                    description = "eius"
                ),
                Product(
                    id = 2081,
                    name = "Cleo Craft",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=postea",
                    description = "propriae"
                ),
                Product(
                    id = 6185,
                    name = "Marissa Bartlett",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=harum",
                    description = "pri"
                ),
                Product(
                    id = 5080,
                    name = "Blanca Bradley",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=tincidunt",
                    description = "deserunt"
                ),
                Product(
                    id = 3881,
                    name = "Cleveland Baird",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=veniam",
                    description = "eius"
                ),
                Product(
                    id = 2081,
                    name = "Cleo Craft",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=postea",
                    description = "propriae"
                ),
                Product(
                    id = 6185,
                    name = "Marissa Bartlett",
                    type = Type.PERSONAL,
                    imageUrl = "https://www.google.com/#q=harum",
                    description = "pri"
                ),
            ) }

            withContext(Main) {
                _isLoading.update { false }
            }
        }
    }
}