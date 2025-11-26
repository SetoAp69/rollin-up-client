package com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval

data class PermitApprovalFormData(
    val isApproved: Boolean = false,
    val approvalNote: String = "",
    val approvalNoteError: String? = null,
) {
    val isValid
        get() = approvalNoteError == null
}
