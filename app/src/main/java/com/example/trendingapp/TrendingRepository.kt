package com.example.trendingapp

import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.TrendingListResponse
import com.example.trendingapp.model.Trending
import kotlinx.coroutines.flow.Flow

interface TrendingRepository {
    suspend fun fetchTrendingRepos(forceFetch: Boolean) : Answer<Flow<List<Trending>>>
}