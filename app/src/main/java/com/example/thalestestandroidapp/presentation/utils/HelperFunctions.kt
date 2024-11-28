package com.example.thalestestandroidapp.presentation.utils

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.thalestestandroidapp.domain.models.Type
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

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

fun Fragment.observerScope(action: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action()
        }
    }
}

fun Uri.toFile(context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(this)
    val tempFile = File.createTempFile("temp", ".jpg")
    return try {
        tempFile.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
        tempFile.deleteOnExit()
        inputStream?.close()
        tempFile
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}