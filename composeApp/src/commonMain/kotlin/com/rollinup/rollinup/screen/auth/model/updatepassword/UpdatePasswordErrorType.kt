package com.rollinup.rollinup.screen.auth.model.updatepassword

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_otp_empty_error
import rollin_up.composeapp.generated.resources.msg_otp_invalid_error

enum class UpdatePasswordErrorType(private val res: StringResource) {
    OTP_INVALID(Res.string.msg_otp_invalid_error),
    OTP_EMPTY(Res.string.msg_otp_empty_error),

    ;

    @Composable
    fun getMessage(): String {
        return stringResource(res)
    }
}