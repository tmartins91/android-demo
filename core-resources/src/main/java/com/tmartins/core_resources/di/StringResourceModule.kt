package com.tmartins.core_resources.di

import android.content.Context
import com.tmartins.core_resources.StringResourceWrapper
import com.tmartins.core_resources.StringResourceWrapperAndroid
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
public object StringResourceModule {

    @ViewModelScoped
    @Provides
    public fun provideStringResources(
        @ApplicationContext context: Context
    ): StringResourceWrapper = StringResourceWrapperAndroid(context)

}