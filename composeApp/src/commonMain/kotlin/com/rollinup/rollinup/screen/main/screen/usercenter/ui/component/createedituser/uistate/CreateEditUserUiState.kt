package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate

import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser.CreateEditUserFormOption

data class CreateEditUserUiState(
    val isLoading: Boolean = false,
    val isLoadingOptions: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isEdit: Boolean = false,
    val formData: CreateEditUserFormData = CreateEditUserFormData(),
    val submitState: Boolean? = null,
    val isStay: Boolean = false,
    val initialDetail:UserDetailEntity = UserDetailEntity(),
    val formOptions: CreateEditUserFormOption = CreateEditUserFormOption(),
)

