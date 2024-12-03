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
                    replaceImageAndUpdateProduct(
                        id = action.id,
                        name = action.name,
                        imageFile = action.image,
                        description = action.description,
                        type = action.type,
                        price = action.price
                    )
                }
            }

            is ProductDetailAction.CreateProduct -> {
                viewModelScope.launch {
                    createImageAndCreateProduct(
                        name = action.name,
                        imageFile = action.imageFile,
                        description = action.description,
                        type = action.type,
                        price = action.price
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

    private suspend fun replaceImageAndUpdateProduct(
        id: Int,
        name: String? = null,
        imageFile: File? = null,
        description: String? = null,
        type: Type? = null,
        price: Double? = null,
    ) {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            if (imageFile != null) {
                when (val replaceProductImageResult = repository.replaceProductImage(
                    id = id,
                    imageFile = imageFile
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
                            imageUrl = replaceProductImageResult.data, // The new url that the backend returned
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
                    type = type,
                    price = price
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
        type: Type? = null,
        price: Double? = null,
    ) {
        product.value?.let { originalProduct ->
            withContext(IO) {
                when (val updateProductResult = repository.updateProduct(
                    id = originalProduct.id,
                    name = name ?: originalProduct.name,
                    imageUrl = imageUrl ?: originalProduct.imageUrl,
                    description = description ?: originalProduct.description,
                    type = type ?: originalProduct.type,
                    price = price ?: originalProduct.price
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

    private suspend fun createImageAndCreateProduct(
        name: String,
        imageFile: File,
        description: String,
        type: Type,
        price: Double
    ) {
        withContext(Main) {
            _isLoading.update { true }
        }

        withContext(IO) {
            when (val createProductImageResult = repository.createProductImage(
                imageFile = imageFile
            )) {
                is Result.Error -> {
                    when (createProductImageResult.error) {
                        NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> {
                            Timber.d("test: coroutine cancelled")
                        }

                        else -> {
                            _productDetailEvents.send(
                                ProductDetailEvents.Error(
                                    createProductImageResult.error.asUiText()
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
                    createProduct(
                        name = name,
                        imageUrl = createProductImageResult.data, // The new url that the backend created
                        description = description,
                        type = type,
                        price = price
                    )

                    withContext(Main) {
                        _isLoading.update { false }
                    }
                }
            }
        }
    }

    private suspend fun createProduct(
        name: String,
        imageUrl: String,
        description: String,
        type: Type,
        price: Double
    ) {
        withContext(IO) {
            when (val createProductResult = repository.createProduct(
                name = name, imageUrl = imageUrl, description = description, type = type, price = price
            )) {
                is Result.Error -> when (createProductResult.error) {
                    NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> {
                        Timber.d("test: coroutine cancelled")
                    }

                    else -> {
                        _productDetailEvents.send(
                            ProductDetailEvents.Error(
                                createProductResult.error.asUiText()
                            )
                        )
                    }
                }

                is Result.Success -> {
                    Timber.d("test: createProductResult.data=${createProductResult.data}")
                    _product.update { createProductResult.data }
                    _productDetailEvents.send(
                        ProductDetailEvents.ProductUpdated(
                            UiText.StringResource(R.string.product_created)
                        )
                    )
                }
            }
        }
    }
}