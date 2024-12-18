package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.data.network.dtos.ProductDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {

    /**
     * Will come in the form of an array of products
     */
    @GET(".")
    suspend fun getAllProducts(): List<ProductDto>

    /**
     * Will return the product that was created
     */
    @POST(".")
    suspend fun postProduct(
        @Body product: ProductDto
    ): ProductDto

    /**
     * Will return the product that was patched. Image will be updated via ProductImageApi.
     */
    @PUT("{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body updatableProduct: ProductDto,
    ): ProductDto

    @DELETE("{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    )

}