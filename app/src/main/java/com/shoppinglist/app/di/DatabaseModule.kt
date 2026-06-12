package com.shoppinglist.app.di

import android.content.Context
import com.shoppinglist.app.core.database.ShoppingDao
import com.shoppinglist.app.core.database.ShoppingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideShoppingDatabase(@ApplicationContext context: Context): ShoppingDatabase {
        return ShoppingDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideShoppingDao(database: ShoppingDatabase): ShoppingDao {
        return database.shoppingDao()
    }
}