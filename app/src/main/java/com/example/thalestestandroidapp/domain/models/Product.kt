package com.example.thalestestandroidapp.domain.models

data class Product(
    val id: Int,
    val name: String,
    val type: Type,
    val imageUrl: String,
    val description: String
)

enum class Type {
    PERSONAL, FAMILY, COMMERCIAL
}

enum class SortOptions {
    NAME_ASC, NAME_DESC
}