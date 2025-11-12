package com.rollinup.apiservice.data.repository.attendance.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rollinup.apiservice.data.mapper.AttendanceMapper
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity

class GetAttendanceListByStudentPagingSource(
    private val id: String,
    private val dataSource: AttendanceApiService,
    private val queryParams: GetAttendanceListByStudentQueryParams,
    private val mapper: AttendanceMapper,
) : PagingSource<Int, AttendanceByStudentEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AttendanceByStudentEntity> {
        val page = params.key ?: 1
        val limit = params.loadSize
        val query = queryParams.copy(limit = limit, page = page).toQueryMap()
        val response = dataSource.getAttendanceListByStudent(
            id = id,
            query = query,
        )
        return when (response) {
            is ApiResponse.Error -> LoadResult.Error(response.e)
            is ApiResponse.Success -> {
                val data = mapper.mapAttendanceListByStudent(response.data.data.data)
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.size < limit) null else page + 1
                )
            }
        }

    }

    override fun getRefreshKey(state: PagingState<Int, AttendanceByStudentEntity>): Int? {
        return state.anchorPosition
    }
}