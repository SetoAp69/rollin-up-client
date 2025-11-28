package com.rollinup.apiservice.data.repository.user.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rollinup.apiservice.data.mapper.UserMapper
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.model.request.user.GetUserQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.user.UserEntity

class GetUserPagingSource(
    private val dataSource: UserApiService,
    private val query: GetUserQueryParams,
    private val mapper: UserMapper,
) : PagingSource<Int, UserEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
        val page = params.key ?: 1
        val limit = params.loadSize
        val queryParams = query.copy(page = page, limit = limit)

        val response = dataSource.getUsersList(queryParams)
        return when (response) {
            is ApiResponse.Error -> LoadResult.Error(response.e)
            is ApiResponse.Success -> {
                val data = mapper.mapGetUserList(response.data.data.data)
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size < limit) null else page + 1
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? {
        return state.anchorPosition
    }
}