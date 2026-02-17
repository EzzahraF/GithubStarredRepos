package com.example.githubstarredrepos.domain.repossitory

import androidx.paging.PagingData
import com.example.githubstarredrepos.domain.model.Repository
import kotlinx.coroutines.flow.Flow
interface GitHubRepository {
    fun getTrendingRepositories(createdAfter: String): Flow<PagingData<Repository>>

}