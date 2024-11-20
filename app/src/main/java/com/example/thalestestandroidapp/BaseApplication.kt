package com.example.thalestestandroidapp

import android.app.Application

class BaseApplication: Application() {
    startKoin{
//        androidLogger()
//        androidContext(this@MainApplication)
//        modules(appModule)
    }
}