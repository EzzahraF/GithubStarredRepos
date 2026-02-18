package com.example.githubstarredrepos.data.repository.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubstarredrepos.data.remote.api.GitHubApiService
import com.example.githubstarredrepos.data.remote.mapper.toDomain
import com.example.githubstarredrepos.domain.model.Repository
import retrofit2.HttpException
import java.io.IOException

class GitHubPagingSource(
    private val apiService: GitHubApiService,
    private val createdAfter: String
) : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {

        val currentPage = params.key ?: 1

        return try {

            val query    = "created:>$createdAfter"
            val response = apiService.searchRepositories(
                query   = query,
                page    = currentPage,
                perPage = params.loadSize
            )

            val repositories = response.items.toDomain()

            LoadResult.Page(
                data    = repositories,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (repositories.isEmpty()) null else currentPage + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}