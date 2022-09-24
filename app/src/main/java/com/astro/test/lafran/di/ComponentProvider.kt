package com.astro.test.lafran.di

import android.content.Context
import com.astro.test.lafran.database.AppDatabase
import com.astro.test.lafran.database.UserDao
import com.astro.test.lafran.feature.userlist.UserListUseCase
import com.astro.test.lafran.feature.userlist.UserListUseCaseImpl
import com.astro.test.lafran.network.ApiInterface
import com.astro.test.lafran.network.Client
import com.astro.test.lafran.repository.Repository
import com.astro.test.lafran.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ComponentProvider {

    @Provides
    @Singleton
    fun provideUserDao(@ApplicationContext context: Context): UserDao =
        AppDatabase.getInstance(context).userDao()

    @Provides
    @Singleton
    fun provideRepository(userDao: UserDao, retrofit: Retrofit): Repository =
        RepositoryImpl(userDao, retrofit.create(ApiInterface::class.java))

    @Provides
    @Singleton
    fun provideUseListUseCase(repository: Repository): UserListUseCase =
        UserListUseCaseImpl(repository)

}