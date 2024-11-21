package com.example.thalestestandroidapp.domain.models

data class Product(
    val name: String,
    val type: Type,
    val imageUrl: String,
    val description: String
)

enum class Type {
    PERSONAL, FAMILY, COMMERCIAL
}