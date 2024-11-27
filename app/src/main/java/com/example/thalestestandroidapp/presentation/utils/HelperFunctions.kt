package com.example.thalestestandroidapp.presentation.utils

import com.example.thalestestandroidapp.domain.models.Type

/**
 * Inline function for when you want to perform an action when multiple variables are not null.
 * Only really makes sense when you have >3 variables or the variables are of the same type
 */
inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit): Unit? {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
        return Unit
    } else return null
}

fun String.toType(): Type? {
    for (type in Type.entries) {
        if (this == type.toString()) return type
    }
    return null
}