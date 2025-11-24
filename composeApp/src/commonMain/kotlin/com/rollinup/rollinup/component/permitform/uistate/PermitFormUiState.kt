package com.rollinup.rollinup.component.permitform.uistate

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.component.permitform.model.PermitFormData

data class PermitFormUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val user: LoginEntity? = null,
    val id: String? = null,
    val formData: PermitFormData = PermitFormData(),
    val submitState: Boolean? = null,
) {
    val isEdit
        get() = id != null
}
