package com.example.githubstarredrepos.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubstarredrepos.domain.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import com.example.githubstarredrepos.domain.usecase.GetTrendingReposUseCase

@HiltViewModel
class ReposViewModel @Inject constructor(
    private val getTrendingReposUseCase: GetTrendingReposUseCase
) : ViewModel() {

    private val TAG = "ReposViewModel"

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val itemCount: Int) : UiState()
        data class Error(val message: String) : UiState()
    }
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val repositories: Flow<PagingData<Repository>> =
        getTrendingReposUseCase(daysAgo = 30)  // ← Utilisation du UseCase
            .cachedIn(viewModelScope)

    init {
        Log.d(TAG, "ViewModel initialized")
        _uiState.value = UiState.Loading
    }

    fun onDataLoaded(itemCount: Int) {
        _uiState.value = UiState.Success(itemCount)
    }

    fun onError(error: Throwable) {
        _uiState.value = UiState.Error(error.message ?: "Unknown error")
    }
    private fun getDateThirtyDaysAgo(): String {
        val calendar   = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        Log.d(TAG,"Fetching repos created after: $date")
        return date
    }
}