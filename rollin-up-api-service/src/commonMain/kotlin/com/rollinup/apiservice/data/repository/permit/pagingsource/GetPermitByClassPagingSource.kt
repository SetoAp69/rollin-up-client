package com.rollinup.apiservice.data.repository.permit.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rollinup.apiservice.data.mapper.PermitMapper
import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.permit.PermitByClassEntity

class GetPermitByClassPagingSource(
    private val datasource: PermitApiService,
    private val mapper: PermitMapper,
    private val classKey: Int,
    private val queryParams: GetPermitListQueryParams,
) : PagingSource<Int, PermitByClassEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PermitByClassEntity> {
        val page = params.key ?: 1
        val limit = params.loadSize
        val queryParams = queryParams.copy(
            limit = limit,
            page = page
        ).toQueryMap()

        val response = datasource.getPermitListByClass(classKey, queryParams)
        return when (response) {
            is ApiResponse.Error -> LoadResult.Error(response.e)
            is ApiResponse.Success -> {
                val data = mapper.mapPermitListByClass(response.data.data.data)
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size < limit) null else page + 1
                )
            }
        }


    }

    override fun getRefreshKey(state: PagingState<Int, PermitByClassEntity>): Int? {
        return state.anchorPosition
    }

}