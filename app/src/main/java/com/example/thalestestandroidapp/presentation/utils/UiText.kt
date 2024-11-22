package com.example.thalestestandroidapp.presentation.utils

import android.content.Context
import androidx.annotation.StringRes

/**
 * Used to handle error-string choosing in ViewModel
 */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}