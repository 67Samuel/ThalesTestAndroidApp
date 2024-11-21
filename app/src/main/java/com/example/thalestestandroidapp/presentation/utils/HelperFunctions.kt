package com.example.thalestestandroidapp.presentation.utils

import android.content.Context
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.domain.util.NetworkError

fun NetworkError.toMessage(context: Context): String {
    val resId = when(this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.UNKNOWN -> R.string.error_unknown
        else -> R.string.error_unknown
    }
    return context.getString(resId)
}