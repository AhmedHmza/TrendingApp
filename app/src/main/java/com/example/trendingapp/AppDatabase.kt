package com.example.trendingapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trendingapp.model.Trending

@Database(entities = [ Trending::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trendingDao(): TrendingDao
}