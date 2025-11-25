package com.rollinup.rollinup.component.profile.profilepopup.uistate

import com.rollinup.apiservice.model.user.UserDetailEntity

data class ProfileDialogUiState(
    val isLoading:Boolean = false,
    val userDetail: UserDetailEntity = UserDetailEntity(),
)
