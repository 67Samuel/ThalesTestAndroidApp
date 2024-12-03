package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.data.mappers.toProduct
import com.example.thalestestandroidapp.data.network.dtos.ProductDto
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import com.example.thalestestandroidapp.domain.network.Repository
import com.example.thalestestandroidapp.domain.util.EmptyResult
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import com.example.thalestestandroidapp.domain.util.asEmptyDataResult
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val productImageApi: ProductImageApi,
) : Repository {
    override suspend fun getAllProducts(): Result<List<Product>, NetworkError> = try {
        val products = productApi.getAllProducts()
        Result.Success(products.map { it.toProduct() })
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun createProduct(
        name: String,
        imageUrl: String,
        description: String,
        type: Type,
        price: Double
    ): Result<Product, NetworkError> = try {
        val postedProduct = productApi.postProduct(
            product = ProductDto.productDtoForCreation(
                name = name,
                imageUrl = imageUrl,
                description = description,
                type = type,
                price = price
            )
        )
        Result.Success(postedProduct.toProduct())
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun updateProduct(
        id: Int,
        name: String,
        imageUrl: String,
        description: String,
        type: Type,
        price: Double
    ): Result<Product, NetworkError> = try {
        val updatedProductDto = productApi.updateProduct(
            id = id,
            updatableProduct = ProductDto(
                id = id,
                name = name,
                type = type.toString(),
                imageUrl = imageUrl,
                description = description,
                price = price
            )
        )
        Result.Success(updatedProductDto.toProduct())
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

    override suspend fun replaceProductImage(
        id: Int,
        imageFile: File
    ): Result<String, NetworkError> =
        try {
            val imageUrl = productImageApi.putProductImage(
                productId = id,
                image = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = imageFile.name,
                    body = imageFile.asRequestBody()
                )
            )
            Result.Success(imageUrl)
        } catch (e: Exception) {
            // The API endpoint for this example project does not accept multipart form data or very
            // large data packets, so it will return a HTTP 400 status code
            if (e is retrofit2.HttpException && (e.code() == 503 || e.code() == 400)) {
                // Return a fake image
                Result.Success("https://www.sandstonecastles.co.uk/wp-content/uploads/2022/12/what-does-success-mean-to-you.jpg")
            } else {
                e.toNetworkErrorResult()
            }
        }

    override suspend fun createProductImage(imageFile: File): Result<String, NetworkError> = try {
        val imageUrl = productImageApi.postProductImage(
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = imageFile.name,
                body = imageFile.asRequestBody()
            )
        )
        Result.Success(imageUrl)
    } catch (e: Exception) {
        // The API endpoint for this example project does not accept multipart form data or very
        // large data packets, so it will return a HTTP 400 status code
        if (e is retrofit2.HttpException && (e.code() == 503 || e.code() == 400)) {
            // Return a fake image
            Result.Success("https://www.sandstonecastles.co.uk/wp-content/uploads/2022/12/what-does-success-mean-to-you.jpg")
        } else {
            e.toNetworkErrorResult()
        }
    }

    override suspend fun deleteProduct(id: Int): EmptyResult<NetworkError> = try {
        productApi.deleteProduct(id)
        Result.Success(Unit).asEmptyDataResult()
    } catch (e: Exception) {
        e.toNetworkErrorResult()
    }

}