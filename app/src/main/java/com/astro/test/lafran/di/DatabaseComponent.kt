package com.astro.test.lafran.di

import android.content.Context
import com.astro.test.lafran.database.AppDatabase
import com.astro.test.lafran.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseComponent {

    @Provides
    @Singleton
    fun provideUserDao(@ApplicationContext context: Context): UserDao =
        AppDatabase.getInstance(context).userDao()

}