package com.rollinup.rollinup.screen.auth.model.login

data class LoginCallback(
    val onUpdateForm: (LoginFormData) -> Unit = {},
    val onLogin: (LoginFormData) -> Unit = {},
    val onGenerateDeviceId: () -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)


class TestClass(){}
