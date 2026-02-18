package com.example.githubstarredrepos.domain.repository

import androidx.paging.PagingData
import com.example.githubstarredrepos.domain.model.Repository
import kotlinx.coroutines.flow.Flow
interface GitHubRepository {
    fun getStarredRepositories(createdAfter: String): Flow<PagingData<Repository>>

}