package com.example.thalestestandroidapp.data.network

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductImageApi {
    /**
     * Uploads a product image to the server, which will then host it in a
     */
    @POST("/{id}")
    @Multipart
    suspend fun replaceProductImage(
        @Part @Path("id") productId: Int,
        @Part image: MultipartBody.Part
    )
}