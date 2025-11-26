package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.permit.PermitByStudentEntity

data class StudentPermitUiState(
    val user: LoginEntity = LoginEntity(),
    val itemList: List<PermitByStudentEntity> = emptyList(),
    val itemSelected: List<PermitByStudentEntity> = emptyList(),
    val searchQuery: String = "",
)
