package com.rollinup.apiservice.data.repository.generalsetting

import com.rollinup.apiservice.model.common.GeneralSetting
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.Flow

interface GeneralSettingRepository {
    fun listen(): Flow<Result<GeneralSetting, NetworkError>>
}