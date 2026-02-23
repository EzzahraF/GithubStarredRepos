package com.example.githubstarredrepos.data.remote.mapper
import com.example.githubstarredrepos.data.remote.dto.RepositoryDto
import com.example.githubstarredrepos.domain.model.Repository

fun RepositoryDto.toDomain(): Repository {
    return Repository(
        id          = this.id,
        name        = this.name,
        description = this.description ?: "No description available",
        stars       = this.stars,
        ownerUsername  = this.owner.login,
        ownerAvatarUrl = this.owner.avatarUrl,
        language=this.language,
        forks=this.forks
    )
}

fun List<RepositoryDto>.toDomain(): List<Repository> {
    return this.map { it.toDomain() }
}