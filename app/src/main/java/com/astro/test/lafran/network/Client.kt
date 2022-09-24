package com.astro.test.lafran.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Client {
    private const val token = "Bearer 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"

    @Provides
    @Singleton
    fun getInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor() { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/vnd.github+json")
                    .addHeader("Authorization", token).build()
                return@Interceptor chain.proceed(request)
            })
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()
    }
}