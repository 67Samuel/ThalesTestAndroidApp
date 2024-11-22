package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.data.mappers.toProduct
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.network.Repository
import com.example.thalestestandroidapp.domain.util.EmptyResult
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.example.thalestestandroidapp.domain.util.asEmptyDataResult
import timber.log.Timber
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : Repository {
    override suspend fun getAllProducts(): Result<List<Product>, NetworkError> = try {
        Timber.d("test: called")
        val products = api.getAllProducts()
        Timber.d("test: products=${products.take(10)}")
        Result.Success(products.map { it.toProduct() })
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun postProduct(product: Product): Result<Product, NetworkError> = try {
        val postedProduct = api.postProduct(product = product)
        Result.Success(postedProduct.toProduct())
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun putProduct(product: Product): Result<Product, NetworkError> = try {
        val postedProduct = api.putProduct(id = product.id, product = product)
        Result.Success(postedProduct.toProduct())
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun deleteProduct(id: Int): EmptyResult<NetworkError> = try {
        api.deleteProduct(id)
        Result.Success(Unit).asEmptyDataResult()
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

}