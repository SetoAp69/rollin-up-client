package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.uistate

import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingFormData

data class GlobalSettingUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val formData: GlobalSettingFormData = GlobalSettingFormData(),
    val generalSetting: GlobalSetting = GlobalSetting(),
    val submitState: Boolean? = null,
)
