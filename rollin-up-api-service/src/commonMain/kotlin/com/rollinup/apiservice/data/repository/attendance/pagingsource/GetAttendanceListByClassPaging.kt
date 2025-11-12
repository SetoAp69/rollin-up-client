package com.rollinup.apiservice.data.repository.attendance.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rollinup.apiservice.data.mapper.AttendanceMapper
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity

class GetAttendanceListByClassPaging(
    val classKey: Int,
    val mapper: AttendanceMapper,
    val dataSource: AttendanceApiService,
    val queryParams: GetAttendanceListByClassQueryParams,
) : PagingSource<Int, AttendanceByClassEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AttendanceByClassEntity> {
        val page = params.key ?: 1
        val limit = params.loadSize
        val query = queryParams.copy(
            limit = limit,
            page = page,
        ).toQueryMap()
        val response = dataSource.getAttendanceListByClass(classKey, query)
        return when (response) {
            is ApiResponse.Error -> LoadResult.Error(response.e)
            is ApiResponse.Success -> {
                val data = mapper.mapAttendanceListByClass(response.data.data.data)
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size < limit) null else page + 1
                )

            }

        }
    }

    override fun getRefreshKey(state: PagingState<Int, AttendanceByClassEntity>): Int? {
        return state.anchorPosition
    }

}