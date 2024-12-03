package com.example.thalestestandroidapp.data.network.dtos

import androidx.annotation.Keep
import com.example.thalestestandroidapp.domain.models.Type
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val name: String,
    val type: String,
    val imageUrl: String,
    val description: String,
    val price: Double
) {
    companion object {
        fun productDtoForCreation(name: String, imageUrl: String, description: String, type: Type, price: Double): ProductDto {
            return ProductDto(
                id = -1,
                name = name,
                type = type.toString(),
                imageUrl = imageUrl,
                description = description,
                price = price
            )
        }
    }
}
