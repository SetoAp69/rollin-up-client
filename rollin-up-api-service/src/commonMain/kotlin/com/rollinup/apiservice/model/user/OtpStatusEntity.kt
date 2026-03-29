package com.rollinup.apiservice.model.user

import com.rollinup.common.utils.Utils.now
import kotlinx.datetime.LocalTime
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
data class OtpStatusEntity(
    val email:String = "",
    val expiredAt: LocalTime = LocalTime.now()
)
