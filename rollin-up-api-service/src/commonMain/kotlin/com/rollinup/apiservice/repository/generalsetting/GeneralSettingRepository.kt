package com.rollinup.apiservice.repository.generalsetting

import com.rollinup.apiservice.model.GeneralSetting
import com.rollinup.apiservice.model.NetworkError
import com.rollinup.apiservice.model.Result
import kotlinx.coroutines.flow.Flow

interface GeneralSettingRepository {
    fun listen(): Flow<Result<GeneralSetting, NetworkError>>
}