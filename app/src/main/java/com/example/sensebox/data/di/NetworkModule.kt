package com.example.sensebox.data.di

import com.example.sensebox.data.network.BoxApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideBoxApiService(): BoxApiService {
       return BoxApiService.create()
    }
}