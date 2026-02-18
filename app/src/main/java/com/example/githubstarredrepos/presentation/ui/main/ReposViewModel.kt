package com.example.githubstarredrepos.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubstarredrepos.domain.model.Repository
import com.example.githubstarredrepos.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    val repositories: Flow<PagingData<Repository>> =
        repository
            .getStarredRepositories(getDateThirtyDaysAgo())
            .cachedIn(viewModelScope)

    private fun getDateThirtyDaysAgo(): String {
        val calendar   = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}