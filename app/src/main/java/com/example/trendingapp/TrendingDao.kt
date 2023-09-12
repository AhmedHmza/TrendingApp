package com.example.trendingapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trendingapp.model.Trending
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
@Dao
interface TrendingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingList(trendingList: List<Trending>)

    @Query("SELECT * FROM trending")
    fun getAllTrending(): Flow<List<Trending>>

    @Query("SELECT * FROM trending")
    fun getTrendingList(): List<Trending>

    @Query("DELETE FROM trending")
    suspend fun removeAll()
}
