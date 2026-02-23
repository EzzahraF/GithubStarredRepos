package com.example.githubstarredrepos.domain.usecase

import androidx.paging.PagingData
import com.example.githubstarredrepos.domain.model.Repository
import com.example.githubstarredrepos.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetTrendingReposUseCase @Inject constructor(
    private val repository: GitHubRepository
) {

    operator fun invoke(daysAgo: Int = 30): Flow<PagingData<Repository>> {
        val createdAfter = getDateNDaysAgo(daysAgo)
        return repository.getStarredRepositories(createdAfter)
    }

    private fun getDateNDaysAgo(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}