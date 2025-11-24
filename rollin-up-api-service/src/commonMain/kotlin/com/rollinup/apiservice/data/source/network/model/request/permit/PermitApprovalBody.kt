package com.rollinup.apiservice.data.source.network.model.request.permit

import com.rollinup.apiservice.Utils.toJsonString

data class PermitApprovalBody(
    val listId: List<String> = emptyList(),
    val approvalNote: String = "",
    val isApproved: Boolean = false,
) {
    fun toHashMap(): HashMap<String, String> {
        val hashMap = hashMapOf<String, String>()

        hashMap["listId"] = listId.toJsonString()
        hashMap["approvalNote"] = approvalNote
        hashMap["isApproved"] = isApproved.toString()

        return hashMap
    }
}
