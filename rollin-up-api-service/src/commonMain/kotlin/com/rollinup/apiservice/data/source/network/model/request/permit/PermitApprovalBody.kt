package com.rollinup.apiservice.data.source.network.model.request.permit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermitApprovalBody(
    @SerialName("listId")
    val listId: List<String> = emptyList(),
    @SerialName("approvalNote")
    val approvalNote: String = "",
    @SerialName("isApproved")
    val isApproved: Boolean = false,
)
