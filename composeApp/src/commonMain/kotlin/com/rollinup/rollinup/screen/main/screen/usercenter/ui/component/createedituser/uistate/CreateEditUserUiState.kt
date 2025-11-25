package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate

import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormOption

data class CreateEditUserUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isEdit: Boolean = false,
    val formData: CreateEditUserFormData = CreateEditUserFormData(),
    val submitState: Boolean? = null,
    val isStay: Boolean = false,
    val formOptions: CreateEditUserFormOption = CreateEditUserFormOption(),
) {
    val isStudentRole
        get() =
            formData.role == formOptions
                .role
                .find {
                    it.label.equals("Student", true)
                }
                ?.value

}
