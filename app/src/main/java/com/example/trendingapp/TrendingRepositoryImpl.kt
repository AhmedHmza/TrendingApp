package com.example.trendingapp

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.TrendingListResponse
import com.example.trendingapp.model.Trending
import com.example.trendingapp.services.TrendingServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val trendingServices: TrendingServices,
    private val ioDispatcher: CoroutineDispatcher,
    private val trendingDao: TrendingDao
) : TrendingRepository {

    override suspend fun fetchTrendingRepos(forceFetch: Boolean): Answer<Flow<List<Trending>>> =
        withContext(ioDispatcher) {
            Log.d("hamza", "size = "+trendingDao.getTrendingList())
            if (forceFetch || trendingDao.getTrendingList().isEmpty()) {
                trendingServices.fetchTrendingList().run {
                    if (isSuccessful && body() != null) {
                        body()?.let {
                            trendingDao.removeAll()
                            trendingDao.insertTrendingList(it.toTrendingList())
                        }

                        Answer.Success(trendingDao.getAllTrending())
                    } else {
                        trendingDao.removeAll()
                        Answer.Error()
                    }
                }
            } else {
                Log.d("hamza","from else")
                Answer.Success(trendingDao.getAllTrending())
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

