package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse
import kotlinx.coroutines.flow.Flow

interface GeneralSettingApiService {
    fun listen(): Flow<ApiResponse<GeneralSettingResponse>>
}