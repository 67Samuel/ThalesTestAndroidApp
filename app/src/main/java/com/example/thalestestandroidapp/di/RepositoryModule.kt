package com.example.thalestestandroidapp.di

import com.example.thalestestandroidapp.data.network.ProductRepositoryImpl
import com.example.thalestestandroidapp.domain.network.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


object RepositoryModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

        @Binds
        @Singleton
        abstract fun bindRepository(
            repositoryImpl: ProductRepositoryImpl
        ): Repository
    }

}