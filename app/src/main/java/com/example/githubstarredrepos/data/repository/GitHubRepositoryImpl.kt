package com.example.githubstarredrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubstarredrepos.data.remote.api.GitHubApiService
import com.example.githubstarredrepos.data.remote.mapper.toDomain
import com.example.githubstarredrepos.data.repository.paging.GitHubPagingSource
import com.example.githubstarredrepos.domain.model.Repository
import com.example.githubstarredrepos.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GitHubRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService
) : GitHubRepository {
     override fun getStarredRepositories(createdAfter: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                initialLoadSize = 30
            ),
            pagingSourceFactory = {
                GitHubPagingSource(apiService, createdAfter)
            }
        ).flow
    }
    override suspend fun getRepoDetails(owner: String, name: String): Repository {
        val dto = apiService.getRepoDetails(owner, name)
        return dto.toDomain()
    }
}