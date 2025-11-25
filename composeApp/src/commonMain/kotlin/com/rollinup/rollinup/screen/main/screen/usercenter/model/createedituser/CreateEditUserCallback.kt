package com.rollinup.rollinup.screen.main.screen.usercenter.model.createedituser

data class CreateEditUserCallback(
    val onUpdateForm: (CreateEditUserFormData) -> Unit = {},
    val onResetForm: () -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onSubmit: (CreateEditUserFormData, Boolean) -> Unit = { _, _ -> },
    val onValidateForm: (CreateEditUserFormData, Boolean) -> Boolean = { _, _ -> false},
    val onCheckEmail: (String) -> Unit = {},
    val onCheckUserName: (String) -> Unit = {},
    val onToggleStay: (Boolean) -> Unit = {},
)
