package com.example.thalestestandroidapp.presentation

import android.app.Application
import com.example.thalestestandroidapp.BuildConfig
import timber.log.Timber
import timber.log.Timber.Forest.plant

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "${element.fileName.truncateFileName()}:${element.lineNumber}#${element.methodName.truncateMethodName()}"
                }
            })
        }
    }



    private fun String.truncateFileName(maxChars: Int = 15): String {
        val name = substringBefore('.')
            .replace("ViewModel", "VM")
            .replace("Fragment", "Frag")
            .replace("Application", "App")
        return if (name.length > maxChars)
            name.substring(0 until maxChars)
        else
            name
    }

    private fun String.truncateMethodName(): String {
        return substringBefore("\$lambda$")
    }

}