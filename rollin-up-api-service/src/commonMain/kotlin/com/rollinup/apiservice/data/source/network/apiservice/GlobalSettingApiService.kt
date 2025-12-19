package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import kotlinx.coroutines.flow.Flow

interface GlobalSettingApiService {
    fun listen(): Flow<ApiResponse<Unit>>

    suspend fun getGlobalSetting(): ApiResponse<GetGlobalSettingResponse>

    suspend fun editGlobalSetting(body: EditGlobalSettingBody): ApiResponse<Unit>
}