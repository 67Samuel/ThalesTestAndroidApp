package com.example.thalestestandroidapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val type: Type,
    val imageUrl: String,
    val description: String
): Parcelable {
    companion object {
        val EMPTY: Product
            get() = Product(
                id = 0,
                name = "",
                type = Type.PERSONAL,
                imageUrl = "",
                description = ""
            )
    }
}

enum class Type {
    PERSONAL, FAMILY, COMMERCIAL
}

enum class SortOptions {
    NAME_ASC, NAME_DESC
}