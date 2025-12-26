package com.rollinup.rollinup.component.profile.profilepopup.uistate

import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.profile.profilepopup.model.EditProfileFormData

data class ProfileDialogUiState(
    val isLoading: Boolean = false,
    val showEdit: Boolean = false,
    val userDetail: UserDetailEntity = UserDetailEntity(),
    val isLoadingOverlay: Boolean = false,
    val isEdit: Boolean = false,
    val editState: Boolean? = null,
    val formData: EditProfileFormData = EditProfileFormData(),
) {
    val genderOptions
        get() = Gender.entries.map {
            OptionData(it.label, it.value)
        }
}
