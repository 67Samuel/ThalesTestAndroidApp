package com.example.thalestestandroidapp.data.network.dtos

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val name: String,
    val type: String,
    val imageUrl: String,
    val description: String
)
