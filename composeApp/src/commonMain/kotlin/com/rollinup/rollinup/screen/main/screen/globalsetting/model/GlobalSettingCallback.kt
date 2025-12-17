package com.rollinup.rollinup.screen.main.screen.globalsetting.model

data class GlobalSettingCallback(
    val onUpdateForm:(GlobalSettingFormData)->Unit = {},
    val onSubmit:(GlobalSettingFormData)->Unit = {},
    val onResetMessageState:()->Unit = {},
    val onRefresh:()->Unit = {}
)
