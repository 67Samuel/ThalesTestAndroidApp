package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.data.network.dtos.ProductDto
import com.example.thalestestandroidapp.domain.models.Product
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
    @GET("/product")
    suspend fun getAllProducts(): List<ProductDto>

    /**
     * Will return the product that was created
     */
    @POST("/product")
    suspend fun postProduct(
        @Body product: Product
    ): ProductDto

    /**
     * Will return the product that was patched
     */
    @PUT("/product/{id}")
    suspend fun putProduct(
        @Path("id") id: Int,
        @Body product: Product
    ): ProductDto

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    )

}