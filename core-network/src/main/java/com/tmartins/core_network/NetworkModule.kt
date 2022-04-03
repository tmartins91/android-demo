package com.tmartins.core_network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @RetrofitUnauthenticated
    @Singleton
    fun provideRetrofitUnauthenticated(@OkHttpUnauthenticated httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @LoggingOkHttpInterceptor
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @OkHttpUnauthenticated
    @Provides
    @Singleton
    fun provideHttpClientUnauthenticated(
        @LoggingOkHttpInterceptor loggingInterceptor: Interceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            client.addInterceptor(loggingInterceptor)
        }

        return client.build()
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class LoggingOkHttpInterceptor

@Retention(AnnotationRetention.BINARY)
@Qualifier
public annotation class OkHttpUnauthenticated

@Retention(AnnotationRetention.BINARY)
@Qualifier
public annotation class RetrofitUnauthenticated
