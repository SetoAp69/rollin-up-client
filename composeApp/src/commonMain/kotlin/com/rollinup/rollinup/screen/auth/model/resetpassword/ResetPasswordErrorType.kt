package com.rollinup.rollinup.screen.auth.model.resetpassword

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_email_contain_space_error
import rollin_up.composeapp.generated.resources.msg_email_empty_error
import rollin_up.composeapp.generated.resources.msg_email_invalid_error
import rollin_up.composeapp.generated.resources.msg_otp_empty_error
import rollin_up.composeapp.generated.resources.msg_otp_invalid_error

enum class ResetPasswordErrorType(private val res: StringResource) {
    OTP_EMPTY(Res.string.msg_otp_empty_error),
    OTP_INVALID(Res.string.msg_otp_invalid_error),
    EMAIL_CONTAIN_SPACE(Res.string.msg_email_contain_space_error),
    EMAIL_INVALID(Res.string.msg_email_invalid_error),
    EMAIL_EMPTY(Res.string.msg_email_empty_error);

    @Composable
    fun getMessage(): String {
        return stringResource(res)
    }
}