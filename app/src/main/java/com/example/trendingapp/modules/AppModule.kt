package com.example.trendingapp.di.modules

import com.example.trendingapp.TrendingRepository
import com.example.trendingapp.TrendingRepositoryImpl
import com.example.trendingapp.services.TrendingServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher)
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideTrendingRepository(trendingServices: TrendingServices, couroutineDispatcher: CoroutineDispatcher): TrendingRepository = TrendingRepositoryImpl(trendingServices, couroutineDispatcher)

}
