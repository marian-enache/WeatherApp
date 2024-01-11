package com.example.weatherapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        NetworkModule::class,
        UseCasesModule::class]
)
@InstallIn(SingletonComponent::class)
object AppModule