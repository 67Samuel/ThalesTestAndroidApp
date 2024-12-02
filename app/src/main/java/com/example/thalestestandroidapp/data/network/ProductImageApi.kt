package com.example.thalestestandroidapp.data.network

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductImageApi {
    /**
     * Replaces the image in the server, and returns the URL.
     */
    @PUT("{id}")
    @Multipart
    suspend fun putProductImage(
        @Path("id") productId: Int,
        @Part image: MultipartBody.Part
    ): String

    /**
     * Uploads a product image to the server, which will then host it in a database, create a URL for it, and return that URL.
     */
    @POST(".")
    @Multipart
    suspend fun postProductImage(
        @Part image: MultipartBody.Part
    ): String
}