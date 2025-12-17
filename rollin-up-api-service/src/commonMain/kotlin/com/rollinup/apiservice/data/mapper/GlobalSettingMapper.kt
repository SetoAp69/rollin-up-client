package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.data.source.network.model.response.globalsetting.GetGlobalSettingResponse
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse
import com.rollinup.apiservice.model.common.GlobalSetting

class GlobalSettingMapper {
    fun mapGlobalSetting(
        data: GeneralSettingResponse,
    ) = GlobalSetting(
        semesterStart = data.semesterStart,
        semesterEnd = data.semesterEnd,
        updatedAt = data.updatedAt,
        _schoolPeriodStart = data.schoolPeriodStart,
        _schoolPeriodEnd = data.schoolPeriodEnd,
        _checkInPeriodStart = data.checkInPeriodStart,
        _checkInPeriodEnd = data.checkInPeriodEnd,
        latitude = data.latitude,
        longitude = data.longitude,
        radius = data.radius,
    )

    fun mapGetGlobalSettingResponse(
        data: GetGlobalSettingResponse.Data,
    ): GlobalSetting {
        return GlobalSetting(
            semesterStart = data.semesterStart,
            semesterEnd = data.semesterEnd,
            updatedAt = data.updatedAt,
            _schoolPeriodStart = data.schoolPeriodStart,
            _schoolPeriodEnd = data.schoolPeriodEnd,
            _checkInPeriodStart = data.checkInPeriodStart,
            _checkInPeriodEnd = data.checkInPeriodEnd,
            latitude = data.latitude,
            longitude = data.longitude,
            radius = data.radius,
        )
    }
}