package com.tmartins.core_resources.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
public object DispatcherModule {

    @DefaultDispatcher
    @Provides
    public fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    public fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    public fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
public annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
public annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
public annotation class MainDispatcher