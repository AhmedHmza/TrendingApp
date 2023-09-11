package com.example.trendingapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.MainCoroutineRule
import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.Answer.Success
import com.example.trendingapp.dto.TrendingListResponse
import com.example.trendingapp.services.TrendingServices
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
        trendingRepository = TrendingRepositoryImpl(api, ioDispatcher)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchTrendingRepos returns error`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText()))

        val result = trendingRepository.fetchTrendingRepos()

        assert(result is Answer.Error)
    }

    @Test
    fun `fetchTrendingRepos returns success`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText()))
        val max: TrendingListResponse = Gson().fromJson(javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText(), TrendingListResponse::class.java)

        val result = trendingRepository.fetchTrendingRepos()

        assert(result is Success)
        if(result is Success){
            val list = max.toTrendingList()
            assertEquals(list.size, result.data.size)
        }
    }
}