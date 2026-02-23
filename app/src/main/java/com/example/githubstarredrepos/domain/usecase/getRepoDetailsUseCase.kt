package com.example.githubstarredrepos.domain.usecase

import com.example.githubstarredrepos.domain.model.Repository
import com.example.githubstarredrepos.domain.repository.GitHubRepository
import javax.inject.Inject

class GetRepoDetailsUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(owner: String, repoName: String): Result<Repository> {
        return try {
            val repo = repository.getRepoDetails(owner, repoName)
            Result.success(repo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}