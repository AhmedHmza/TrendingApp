package com.example.trendingapp

import com.example.trendingapp.model.Trending

data class MainViewModelUIState(
    val trendingList: List<Trending> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false
    )
