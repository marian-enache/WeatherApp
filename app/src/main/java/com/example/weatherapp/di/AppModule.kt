package com.example.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        UseCasesModule::class]
)
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    internal fun providesDispatchersProvider(dispatchersProvider: DispatchersProviderImpl): DispatchersProvider = dispatchersProvider
}