package com.example.thalestestandroidapp.domain.network

import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import com.example.thalestestandroidapp.domain.util.EmptyResult
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result
import java.io.File

interface Repository {
    suspend fun getAllProducts(): Result<List<Product>, NetworkError>

    suspend fun postProduct(
        name: String,
        description: String,
        type: Type
    ): Result<Product, NetworkError>

    suspend fun updateProduct(
        id: Int,
        name: String,
        imageUrl: String,
        description: String,
        type: Type
    ): Result<Product, NetworkError>

    suspend fun replaceProductImage(
        id: Int,
        imageFile: File
    ): Result<String, NetworkError>

    suspend fun deleteProduct(
        id: Int
    ): EmptyResult<NetworkError>
}