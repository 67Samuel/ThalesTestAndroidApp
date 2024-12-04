package com.example.thalestestandroidapp.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.SortOption
import com.example.thalestestandroidapp.domain.models.SortOption.*
import com.example.thalestestandroidapp.domain.network.Repository
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.example.thalestestandroidapp.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _sortOption = MutableStateFlow(NAME_ASC)
    val sortOption = _sortOption.asStateFlow()

    /**
     * Always maintains the latest full (unfiltered) product list
     */
    private var _fullProductList = emptyList<Product>()

    private val _productList = MutableStateFlow(emptyList<Product>())
    val productList = _productList.asStateFlow()

    private val _productListEvents = Channel<ProductListEvents>()
    val events = _productListEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadProducts()
        }
    }

    fun onAction(action: ProductListAction) {
        when (action) {
            is ProductListAction.OnProductClick -> {
                viewModelScope.launch {
                    _productListEvents.send(
                        ProductListEvents.NavigateToProductDetails(action.product)
                    )
                }
            }

            ProductListAction.RefreshProducts -> viewModelScope.launch {
                loadProducts()
            }

            is ProductListAction.SortProducts -> viewModelScope.launch {
                sortProducts(action.sortBy)
            }

            is ProductListAction.FilterProducts -> filterProducts(action.text)
        }
    }

    private fun sortProducts(sortOption: SortOption) = when(sortOption) {
        NAME_ASC -> {
            _fullProductList.sortedBy { product -> product.name }
            _productList.update { it.sortedBy { product -> product.name } }
        }
        NAME_DESC -> {
            _fullProductList.sortedByDescending { product -> product.name }
            _productList.update { it.sortedByDescending { product -> product.name } }
        }
        PRICE_ASC -> {
            _fullProductList.sortedBy { product -> product.price }
            _productList.update { it.sortedBy { product -> product.price } }
        }
        PRICE_DESC -> {
            _fullProductList.sortedByDescending { product -> product.price }
            _productList.update { it.sortedByDescending { product -> product.price } }
        }
        DEFAULT -> {
            _fullProductList.sortedBy { product -> product.id }
            _productList.update { it.sortedBy { product -> product.id } }
        }
    }
    
    private fun filterProducts(text: String) {
        _productList.update {
            _fullProductList.filter { product ->
                product.name.contains(text, ignoreCase = true)
            }
        }
    }

    private suspend fun loadProducts() {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            when(val result = repository.getAllProducts()) {
                is Result.Error -> when(result.error) {
                    NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> {
                        Timber.d("test: coroutine cancelled")
                    }
                    else -> _productListEvents.send(ProductListEvents.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    _productList.update { result.data }
                    _fullProductList = result.data
                }
            }

            withContext(Main) {
                _isLoading.update { false }
            }
        }
    }
}