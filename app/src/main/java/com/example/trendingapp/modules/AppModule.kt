package com.example.trendingapp.modules

import android.content.Context
import androidx.room.Room
import com.example.trendingapp.AppDatabase
import com.example.trendingapp.TrendingDao
import com.example.trendingapp.TrendingRepository
import com.example.trendingapp.TrendingRepositoryImpl
import com.example.trendingapp.services.TrendingServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher)
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideTrendingRepository(trendingServices: TrendingServices, couroutineDispatcher: CoroutineDispatcher, trendingDao: TrendingDao): TrendingRepository = TrendingRepositoryImpl(trendingServices, couroutineDispatcher, trendingDao)

    @Provides
    @Singleton
    fun provideAppDao(
        @ApplicationContext appContext
        : Context
    ): AppDatabase {

        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, "Trending"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTrendingDao(database: AppDatabase): TrendingDao {
        return database.trendingDao()
    }
}
