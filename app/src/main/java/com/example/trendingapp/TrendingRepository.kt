package com.example.trendingapp

import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.TrendingListResponse
import com.example.trendingapp.model.Trending

interface TrendingRepository {
    suspend fun fetchTrendingRepos() : Answer<List<Trending>>
}