package com.rollinup.apiservice.mapper

import com.rollinup.apiservice.model.GeneralSetting
import com.rollinup.apiservice.source.network.model.response.sse.GeneralSettingResponse

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