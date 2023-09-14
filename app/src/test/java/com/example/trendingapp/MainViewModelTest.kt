package com.example.trendingapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.MainCoroutineRule
import com.example.trendingapp.dto.Answer
import com.example.trendingapp.dto.Answer.Success
import com.example.trendingapp.dto.TrendingListResponse
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutinesApi = MainCoroutineRule()

    @Mock
    private lateinit var trendingRepository: TrendingRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(trendingRepository)
    }

    @Test
    fun shouldUpdateListWhenRepoReturnsSuccess() = runBlocking {
        val trendingReposResponse: TrendingListResponse = Gson().fromJson(
            javaClass.classLoader?.getResource("fetch_trending_response.json")!!.readText(),
            TrendingListResponse::class.java
        )
        `when`(trendingRepository.fetchTrendingRepos(false)).thenReturn(
            Success(flow { emit(trendingReposResponse.toTrendingList()) })
        )

        viewModel.fetch(false)

        val uiState = viewModel.uiState.value
        assert(!uiState.isLoading)
        assert(!uiState.isError)
        assert(viewModel.trendingList != null)
    }

    @Test
    fun shouldUpdateStateWhenRepositoryReturnsError() = runBlocking {
        `when`(trendingRepository.fetchTrendingRepos(false)).thenReturn(Answer.Error())

        viewModel.fetch(false)

        val uiState = viewModel.uiState.value
        assert(uiState.isError)
    }

    @Test
    fun shouldLoadErrorOnToolbarIconClicked() {
        viewModel.onToolbarIconClicked()

        val uiState = viewModel.uiState.value
        assert(uiState.isError)
    }

}
