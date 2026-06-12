package com.shoppinglist.app.di

import com.shoppinglist.app.feature.shoppinglist.data.repository.ShoppingRepositoryImpl
import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShoppingRepository(
        impl: ShoppingRepositoryImpl
    ): ShoppingRepository
}