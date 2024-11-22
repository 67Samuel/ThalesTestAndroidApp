package com.example.thalestestandroidapp.presentation.utils

import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.domain.util.NetworkError
import com.example.thalestestandroidapp.domain.util.Result

fun NetworkError.asUiText(): UiText {
    return when (this) {
        NetworkError.RemoteNetworkError.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.the_request_timed_out
        )

        NetworkError.RemoteNetworkError.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.youve_hit_your_rate_limit
        )

        NetworkError.RemoteNetworkError.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        NetworkError.RemoteNetworkError.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        NetworkError.RemoteNetworkError.CLIENT_ERROR -> UiText.StringResource(
            R.string.client_error
        )

        NetworkError.RemoteNetworkError.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )

        NetworkError.GeneralNetworkError.UNKNOWN -> UiText.StringResource(
            R.string.unknown_error
        )

        NetworkError.GeneralNetworkError.COROUTINE_CANCELLATION -> UiText.StringResource(
            R.string.unknown_error
        )
    }
}

fun Result.Error<NetworkError>.asErrorUiText(): UiText {
    return error.asUiText()
}