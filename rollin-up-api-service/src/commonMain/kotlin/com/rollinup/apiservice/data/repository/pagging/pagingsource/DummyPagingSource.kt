package com.rollinup.apiservice.data.repository.pagging.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.paging.PagingDummyEntity
import com.rollinup.apiservice.data.source.network.apiservice.PagingDummyApi
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse


class DummyPagignSource(
    private val dataSource: PagingDummyApi,
    //queryParams
    //
) : PagingSource<Int, PagingDummyEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PagingDummyEntity> {
        val page = params.key ?: 1
        val limit = params.loadSize
        L.wtf { "page :$page, limi:$limit" }
        val response = dataSource.getPagingDummy(
            page = page,
            size = limit,
        )
        return when (response) {
            is ApiResponse.Error -> LoadResult.Error(response.e)
            is ApiResponse.Success -> {
                val data = response.data.data.map {
                    PagingDummyEntity(
                        id = it.id,
                        title = it.title,
                        price = it.price
                    )
                }

                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (data.size < limit) null else page.plus(1)
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PagingDummyEntity>): Int? {
        return state.anchorPosition
    }

}

