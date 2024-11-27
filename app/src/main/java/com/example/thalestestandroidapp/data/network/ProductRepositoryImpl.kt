package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.data.mappers.toProduct
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.network.Repository
import com.example.thalestestandroidapp.domain.util.EmptyResult
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.example.thalestestandroidapp.domain.util.asEmptyDataResult
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val productImageApi: ProductImageApi,
) : Repository {
    override suspend fun getAllProducts(): Result<List<Product>, NetworkError> = try {
        Timber.d("test: called")
        val products = productApi.getAllProducts()
        Timber.d("test: products=${products.take(10)}")
        Result.Success(products.map { it.toProduct() })
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun postProduct(product: Product): Result<Product, NetworkError> = try {
        val postedProduct = productApi.postProduct(product = product)
        Result.Success(postedProduct.toProduct())
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun putProduct(product: Product): Result<Product, NetworkError> = try {
        val postedProduct = productApi.putProduct(id = product.id, product = product)
        Result.Success(postedProduct.toProduct())
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun replaceProductImage(id: Int, imageFile: File): EmptyResult<NetworkError> = try {
        productImageApi.replaceProductImage(
            productId = id,
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = imageFile.name,
                body = imageFile.asRequestBody()
            )
        )
        Result.Success(Unit).asEmptyDataResult()
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun deleteProduct(id: Int): EmptyResult<NetworkError> = try {
        productApi.deleteProduct(id)
        Result.Success(Unit).asEmptyDataResult()
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

}