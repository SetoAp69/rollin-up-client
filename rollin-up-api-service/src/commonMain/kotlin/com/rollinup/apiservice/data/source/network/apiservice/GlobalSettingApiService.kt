package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse
import kotlinx.coroutines.flow.Flow

interface GlobalSettingApiService {
    fun listen(): Flow<ApiResponse<GeneralSettingResponse>>

    suspend fun getGlobalSetting(): ApiResponse<GetGlobalSettingResponse>

    suspend fun editGlobalSetting(body: EditGlobalSettingBody): ApiResponse<Unit>
}