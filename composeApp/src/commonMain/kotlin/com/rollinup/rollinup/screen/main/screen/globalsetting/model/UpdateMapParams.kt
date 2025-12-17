package com.rollinup.rollinup.screen.main.screen.globalsetting.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMapParams(
    @SerialName("lat")
    val lat: Double = 0.0,
    @SerialName("long")
    val long: Double = 0.0,
    @SerialName("rad")
    val rad: Double = 0.0,
)
