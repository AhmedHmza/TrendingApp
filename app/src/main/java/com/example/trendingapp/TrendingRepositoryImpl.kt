package com.example.trendingapp

import androidx.annotation.VisibleForTesting
import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.TrendingListResponse
import com.example.trendingapp.model.Trending
import com.example.trendingapp.services.TrendingServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val trendingServices: TrendingServices,
    private val ioDispatcher: CoroutineDispatcher
) :
    TrendingRepository {


    override suspend fun fetchTrendingRepos(): Answer<List<Trending>> =
        withContext(ioDispatcher) {
            trendingServices.fetchTrendingList().run {
                if (isSuccessful && body() != null) {
                    val list = body()!!.toTrendingList()

                    Answer.Success(list)
                } else {
                    Answer.Error()
                }
            }
        }
}


@VisibleForTesting
fun TrendingListResponse.toTrendingList(): List<Trending> {
    return items.map { item ->
        Trending(
            userName = item.owner.login,
            projectName = item.name,
            description = item.description,
            language = item.language,
            starCount = item.stargazers_count.toString()
        )
    }
}

