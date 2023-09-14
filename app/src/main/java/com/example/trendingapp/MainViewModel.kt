package com.example.trendingapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingapp.dto.Answer
import com.example.trendingapp.model.Trending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val trendingRepository: TrendingRepository) :
    ViewModel() {
    lateinit var trendingList: Flow<List<Trending>>
    private val _uiState = MutableStateFlow(MainViewModelUIState())
    val uiState: StateFlow<MainViewModelUIState>
        get() = _uiState.asStateFlow()

    fun fetch(forceFetch: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            trendingRepository.fetchTrendingRepos(forceFetch).let { response ->
                if (response is Answer.Success) {
                    trendingList = response.data

                    _uiState.update {
                        it.copy(
                            isError = false,
                            isLoading = false,
                        )
                    }
                }
                if (response is Answer.Error) {
                    _uiState.update {
                        it.copy(
                            isError = true
                        )
                    }
                }
            }

        }
    }

    fun onToolbarIconClicked() {
        _uiState.update {
            it.copy(
                isError = true
            )
        }
    }

}