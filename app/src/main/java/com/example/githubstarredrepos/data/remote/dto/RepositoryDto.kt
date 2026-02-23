package com.example.githubstarredrepos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RepositoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("owner")
    val owner: OwnerDto,
    @SerializedName("language")
    val language: String?,
    @SerializedName("forks")
    val forks:Int
)