@file:OptIn(KoinExperimentalAPI::class)

package com.example.thalestestandroidapp

import com.example.thalestestandroidapp.di.appModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        appModule.verify()
    }
}