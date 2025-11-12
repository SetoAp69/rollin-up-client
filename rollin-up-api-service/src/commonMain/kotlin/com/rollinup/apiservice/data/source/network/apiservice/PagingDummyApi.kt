package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.paging.GetPagingDummyResponse

interface PagingDummyApi {
    suspend fun getPagingDummy(
        page: Int = 1,
        size: Int = 10,
    ): ApiResponse<GetPagingDummyResponse>
}