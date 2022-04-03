package com.tmartins.feature_currencies.di

import com.tmartins.core_network.RetrofitUnauthenticated
import com.tmartins.feature_currencies.business.GetAllCurrenciesByBaseUseCase
import com.tmartins.feature_currencies.business.GetAllCurrenciesByBaseUseCaseDefault
import com.tmartins.feature_currencies.business.GetAllCurrenciesUseCase
import com.tmartins.feature_currencies.business.GetAllCurrenciesUseCaseDefault
import com.tmartins.feature_currencies.data.CurrenciesApi
import com.tmartins.feature_currencies.data.CurrenciesRepository
import com.tmartins.feature_currencies.data.CurrenciesRepositoryDefault
import com.tmartins.feature_currencies.navigation.CurrenciesNavigator
import com.tmartins.feature_currencies.navigation.CurrenciesNavigatorDefault
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class CurrenciesModule {

    @ViewModelScoped
    @Binds
    internal abstract fun bindGetAllCurrenciesByBaseUseCase(impl: GetAllCurrenciesByBaseUseCaseDefault): GetAllCurrenciesByBaseUseCase

    @ViewModelScoped
    @Binds
    internal abstract fun bindGetAllCurrenciesUseCase(impl: GetAllCurrenciesUseCaseDefault): GetAllCurrenciesUseCase

    @ViewModelScoped
    @Binds
    internal abstract fun bindCurrenciesRepository(impl: CurrenciesRepositoryDefault): CurrenciesRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal object CurrenciesApiModule {

    @Provides
    @Singleton
    internal fun provideCurrenciesApi(@RetrofitUnauthenticated retrofit: Retrofit): CurrenciesApi =
        retrofit.create(CurrenciesApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
internal object CurrenciesNavigationModule {

    @Provides
    @Singleton
    internal fun provideNavigation(): CurrenciesNavigator = CurrenciesNavigatorDefault()
}