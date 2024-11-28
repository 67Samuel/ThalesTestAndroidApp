package com.example.thalestestandroidapp.presentation.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import com.example.thalestestandroidapp.domain.network.Repository
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.example.thalestestandroidapp.presentation.utils.UiText
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()
    val isUpdatingProduct
        get() = product.value != null

    private val _productDetailEvents = Channel<ProductDetailEvents>()
    val events = _productDetailEvents.receiveAsFlow()

    fun onAction(action: ProductDetailAction) {
        when (action) {
            is ProductDetailAction.UpdateProduct -> {
                viewModelScope.launch {
                    updateProduct(
                        id = action.id,
                        name = action.name,
                        image = action.image,
                        description = action.description,
                        type = action.type
                    )
                }
            }

            is ProductDetailAction.CreateProduct -> {
                viewModelScope.launch {
                    createProduct(
                        name = action.name,
                        description = action.description,
                        type = action.type
                    )
                }
            }

            is ProductDetailAction.OnProductReceived -> {
                viewModelScope.launch {
                    _product.update { action.product }
                }
            }
        }
    }

    private suspend fun updateProduct(
        id: Int,
        name: String? = null,
        image: File? = null,
        description: String? = null,
        type: Type? = null
    ) {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            if (image != null) {
                when (val replaceProductImageResult = repository.replaceProductImage(
                    id = id,
                    imageFile = image
                )) {
                    is Result.Error -> {
                        when (replaceProductImageResult.error) {
                            NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> {
                                Timber.d("test: coroutine cancelled")
                            }

                            else -> {
                                _productDetailEvents.send(
                                    ProductDetailEvents.Error(
                                        replaceProductImageResult.error.asUiText()
                                    )
                                )
                            }
                        }

                        withContext(Main) {
                            _isLoading.update { false }
                        }
                    }

                    is Result.Success -> {
                        // Product DB will be updated at this point
                        updateProduct(
                            name = name,
                            imageUrl = replaceProductImageResult.data, // The new url that the backend created
                            description = description,
                            type = type
                        )

                        withContext(Main) {
                            _isLoading.update { false }
                        }
                    }
                }

            } else {
                updateProduct(
                    name = name,
                    description = description,
                    type = type
                )

                withContext(Main) {
                    _isLoading.update { false }
                }
            }
        }
    }

    private suspend fun updateProduct(
        name: String? = null,
        imageUrl: String? = null,
        description: String? = null,
        type: Type? = null
    ) {
        product.value?.let { originalProduct ->
            withContext(IO) {
                when (val updateProductResult = repository.updateProduct(
                    id = originalProduct.id,
                    name = name ?: originalProduct.name,
                    imageUrl = imageUrl ?: originalProduct.imageUrl,
                    description = description ?: originalProduct.description,
                    type = type ?: originalProduct.type
                )) {
                    is Result.Error -> when (updateProductResult.error) {
                        NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> {
                            Timber.d("test: coroutine cancelled")
                        }

                        else -> {
                            _productDetailEvents.send(
                                ProductDetailEvents.Error(
                                    updateProductResult.error.asUiText()
                                )
                            )
                        }
                    }

                    is Result.Success -> {
                        _product.update { updateProductResult.data }
                        _productDetailEvents.send(
                            ProductDetailEvents.ProductUpdated(
                                UiText.StringResource(R.string.product_updated)
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun createProduct(name: String, description: String, type: Type) {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            when (val result = repository.postProduct(
                name = name,
                description = description,
                type = type
            )) {
                is Result.Error -> when (result.error) {
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