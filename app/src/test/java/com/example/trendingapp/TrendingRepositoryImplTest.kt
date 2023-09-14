package com.example.trendingapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.MainCoroutineRule
import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.Answer.Success
import com.example.trendingapp.dto.TrendingListResponse
import com.example.trendingapp.model.Trending
import com.example.trendingapp.services.TrendingServices
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TrendingRepositoryImplTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutinesApi = MainCoroutineRule()

    private val ioDispatcher = Dispatchers.Main

    private lateinit var trendingRepository: TrendingRepository

    @Mock
    private lateinit var trendingDao: TrendingDao

    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrendingServices::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        trendingRepository = TrendingRepositoryImpl(api, ioDispatcher, trendingDao)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun shouldReturnErrorWhenResponseFromServerFail() = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(404).setBody(
                javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText()
            )
        )

        val result = trendingRepository.fetchTrendingRepos(true)

        verify(trendingDao, times(1)).removeAll()
        assert(result is Answer.Error)
    }

    @Test
    fun shouldSyncWithDbWhenFetchTrendingReposSuccessful(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText()
            )
        )
        val trendingReposResponse: TrendingListResponse = Gson().fromJson(
            javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText(),
            TrendingListResponse::class.java
        )
        val flowTrendingList = flow { emit(trendingReposResponse.toTrendingList()) }
        `when`(trendingDao.getAllTrending()).thenReturn(flowTrendingList)

        val result = trendingRepository.fetchTrendingRepos(true)
        val responseList = (result as Success).data

        assert(result is Success)
        assert(responseList == flowTrendingList)
        verify(trendingDao, times(1)).removeAll()
        verify(trendingDao, times(1)).insertTrendingList(trendingReposResponse.toTrendingList())
    }

    @Test
    fun shouldFetchWhenTrendingRepoListIsEmpty(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText()
            )
        )
        val trendingReposResponse: TrendingListResponse = Gson().fromJson(
            javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText(),
            TrendingListResponse::class.java
        )
        `when`(trendingDao.getAllTrending()).thenReturn(flow { emit(emptyList()) })

        trendingRepository.fetchTrendingRepos(false)

        verify(trendingDao, times(1)).removeAll()
        verify(trendingDao, times(1)).insertTrendingList(trendingReposResponse.toTrendingList())
    }
}