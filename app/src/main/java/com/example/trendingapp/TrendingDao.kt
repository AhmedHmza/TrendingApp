package com.example.trendingapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trendingapp.model.Trending
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingList(trendingList: List<Trending>)

    @Query("SELECT * FROM trending")
    fun getAllTrending(): Flow<List<Trending>>
}
