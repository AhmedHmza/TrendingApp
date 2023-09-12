package com.example.trendingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trending(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String?,
    val projectName: String?,
    val description: String?,
    val language: String?,
    val starCount: String?
)
