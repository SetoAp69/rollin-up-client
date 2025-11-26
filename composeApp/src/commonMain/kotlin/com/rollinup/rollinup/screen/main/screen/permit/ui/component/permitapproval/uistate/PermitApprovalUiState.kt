package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.uistate

import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval.PermitApprovalFormData

data class PermitApprovalUiState(
    val selectedId: List<String> = emptyList(),
    val detail: PermitDetailEntity = PermitDetailEntity(),
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val submitState: Boolean? = null,
    val formData: PermitApprovalFormData = PermitApprovalFormData(),
) {
    val isSingle
        get() = selectedId.size == 1
}
