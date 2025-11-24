package com.rollinup.rollinup.component.permitform.model

data class PermitFormCallback(
    val onSubmit: (PermitFormData, () -> Unit, () -> Unit) -> Unit = { _, _, _ -> },
    val onValidateForm: (PermitFormData) -> Boolean = { false },
    val onUpdateFormData: (PermitFormData) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
