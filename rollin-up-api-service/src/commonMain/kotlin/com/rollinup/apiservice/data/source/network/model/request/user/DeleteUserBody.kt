package com.rollinup.apiservice.data.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserBody(
    @SerialName("listId")
    val listId: List<String> = emptyList(),
)
