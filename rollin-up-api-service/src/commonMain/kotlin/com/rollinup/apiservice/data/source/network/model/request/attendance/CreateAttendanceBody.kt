package com.rollinup.apiservice.data.source.network.model.request.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAttendanceBody(
    @SerialName("id")
    val id:String = "",
    @SerialName("date")
    val date:String = "",
    @SerialName("checkInAt")
    val checkInAt:Long = 0L
)
