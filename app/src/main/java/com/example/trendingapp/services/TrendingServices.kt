package com.example.trendingapp.services

import com.example.trendingapp.dto.TrendingListResponse
import retrofit2.Response
import retrofit2.http.GET

interface TrendingServices {
    @GET("search/repositories?q=language=+sort:starsSSSS")
    suspend fun fetchTrendingList(): Response<TrendingListResponse?>
}