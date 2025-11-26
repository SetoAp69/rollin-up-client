package com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval

data class PermitApprovalCallback(
    val onValidate: (PermitApprovalFormData) -> Boolean = {false},
    val onSubmit: (PermitApprovalFormData) -> Unit = {},
    val onUpdateFormData: (PermitApprovalFormData) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
