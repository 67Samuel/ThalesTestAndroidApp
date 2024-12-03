package com.example.thalestestandroidapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File


@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val type: Type,
    val imageUrl: String,
    val description: String
): Parcelable {
    companion object {
        fun newProduct(name: String, imageFile: File, description: String, type: Type): Product {
            return Product(
                id = -1,
                name = name,
                type = type,
                imageUrl = "",
                description = description
            )
        }
    }
}

enum class Type {
    PERSONAL, FAMILY, COMMERCIAL
}

enum class SortOption {
    DEFAULT, NAME_ASC, NAME_DESC
}