package com.example.thalestestandroidapp.data.mappers

import com.example.thalestestandroidapp.data.network.dtos.ProductDto
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        /**
         * The test API I am using creates data with type values like "type 1" etc, so I want to
         * pretend that it actually returns something that can be converted to one of the 3 types
         * I have specified in this app.
         */
        type = try {
            Type.valueOf(type)
        } catch (_: IllegalArgumentException) {
            Type.entries.random()
        },
        imageUrl = imageUrl,
        description = description
    )
}