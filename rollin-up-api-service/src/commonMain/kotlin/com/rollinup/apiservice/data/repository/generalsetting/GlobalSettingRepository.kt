package com.rollinup.apiservice.data.repository.generalsetting

import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.Flow

interface GlobalSettingRepository {
    fun listen(): Flow<Result<Unit, NetworkError>>
    suspend fun getCachedGlobalSetting(): GlobalSetting?
    fun getGlobalSetting(): Flow<Result<GlobalSetting, NetworkError>>
    fun editGlobalSetting(body: EditGlobalSettingBody): Flow<Result<Unit, NetworkError>>
}