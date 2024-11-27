package com.example.thalestestandroidapp.presentation.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import com.example.thalestestandroidapp.domain.network.Repository
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.example.thalestestandroidapp.presentation.product_list.ProductListAction
import com.example.thalestestandroidapp.presentation.product_list.ProductListEvents
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
class ProductDetailViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _product = MutableStateFlow(Product.EMPTY)
    val product = _product.asStateFlow()

    private val _productDetailEvents = Channel<ProductDetailEvents>()
    val events = _productDetailEvents.receiveAsFlow()

    fun onAction(action: ProductDetailAction) {
        when (action) {
            is ProductDetailAction.OnConfirmProductUpdate -> {
                viewModelScope.launch {
                    updateProduct()
                }
            }

            is ProductDetailAction.OnProductReceived -> {
                viewModelScope.launch {
                    _product.update { action.product }
                }
            }
        }
    }

    private suspend fun updateProduct(product: Product) {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            when(val result = repository.postProduct(product)) {
                is Result.Error -> when(result.error) {
                    NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> {
                        Timber.d("test: coroutine cancelled")
                    }
                    else -> _productDetailEvents.send(ProductDetailEvents.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    _product.update { result.data }
                }
            }

            withContext(Main) {
                _isLoading.update { false }
            }
        }
    }
}