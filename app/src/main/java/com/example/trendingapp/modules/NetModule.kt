package com.example.trendingapp.di.modules

import com.example.trendingapp.services.TrendingServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    private val BASE_URL = "https://api.github.com/"


    @Provides
    fun provideRetrofitClient(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideTrendingListService(retrofit: Retrofit) : TrendingServices = retrofit.create(TrendingServices::class.java)

}