package com.example.thalestestandroidapp.data.network

import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductImageApi {
    /**
     * Uploads a product image to the server, which will then host it in a
     */
    @POST("{id}")
    @Multipart
    suspend fun replaceProductImage(
        @Path("id") productId: Int,
        @Part image: MultipartBody.Part
    ): String
}