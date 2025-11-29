package com.rollinup.rollinup.component.profile.profilepopup.model

data class ProfileCallback(
    val onToggleEdit: () -> Unit = {},
    val onUpdateForm: (EditProfileFormData) -> Unit = {},
    val onSubmit: (EditProfileFormData) -> Unit = {},
    val onValidate: (EditProfileFormData) -> Boolean = { false },
    val onResetMessageState: () -> Unit = {},
)