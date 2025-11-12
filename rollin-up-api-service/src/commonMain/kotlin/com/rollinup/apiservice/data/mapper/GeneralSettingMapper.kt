package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.model.common.GeneralSetting
import com.rollinup.apiservice.data.source.network.model.response.sse.GeneralSettingResponse

class GeneralSettingMapper {
    fun mapGeneralSetting(
        data: GeneralSettingResponse,
    ) = GeneralSetting(
        semesterStart = data.semesterStart,
        semesterEnd = data.semesterEnd,
        updatedAt = data.updatedAt,
        schoolPeriodStart = data.schoolPeriodStart,
        schoolPeriodEnd = data.schoolPeriodEnd,
        checkInPeriodStart = data.checkInPeriodStart,
        checkInPeriodEnd = data.checkInPeriodEnd,
        latitude = data.latitude,
        longitude = data.longitude,
        radius = data.radius,
    )
}