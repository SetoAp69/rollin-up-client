package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.uistate

import com.rollinup.apiservice.model.permit.PermitDetailEntity

data class PermitDetailUiState(
    val isLoading: Boolean = false,
    val detail: PermitDetailEntity = PermitDetailEntity(),
)
