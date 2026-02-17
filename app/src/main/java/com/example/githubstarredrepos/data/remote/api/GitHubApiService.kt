package com.example.githubstarredrepos.data.remote.api

import com.example.githubstarredrepos.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {
    @GET("searrch/repositoriees")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort : String = "stars",
        @Query("order") order: String= "desc",
        @Query("page") page : Int=1,
        @Query("per_page") perPage: Int=30
    ): SearchResponseDto
}