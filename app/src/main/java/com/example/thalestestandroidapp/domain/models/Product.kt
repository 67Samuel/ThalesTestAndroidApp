package com.example.thalestestandroidapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val type: Type,
    val imageUrl: String,
    val description: String,
    val price: Double
): Parcelable

enum class Type {
    PERSONAL, FAMILY, COMMERCIAL
}

enum class SortOption {
    DEFAULT, NAME_ASC, NAME_DESC, PRICE_ASC, PRICE_DESC
}