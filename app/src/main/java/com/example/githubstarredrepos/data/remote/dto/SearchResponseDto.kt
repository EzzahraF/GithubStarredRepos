package com.example.githubstarredrepos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("items")
    val items: List<RepositoryDto>
)
//quand on utilise xml ou json dans kotlin ( et explique moi xml x'est quoi a quoi sert et pourquoi on l'utilse