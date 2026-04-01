package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.mobile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.password.PasswordErrorType
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.textfield.PasswordTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.time.TimeText
import com.rollinup.rollinup.component.utils.Utils
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordCallback
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordErrorType
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.OTPTextField
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_mail_line_24
import rollin_up.composeapp.generated.resources.label_back_to_login
import rollin_up.composeapp.generated.resources.label_email
import rollin_up.composeapp.generated.resources.label_new_password
import rollin_up.composeapp.generated.resources.label_reenter_email
import rollin_up.composeapp.generated.resources.label_repeat_new_password
import rollin_up.composeapp.generated.resources.label_resend_otp
import rollin_up.composeapp.generated.resources.label_submit
import rollin_up.composeapp.generated.resources.msg_otp_already_sent
import rollin_up.composeapp.generated.resources.ph_email
import rollin_up.composeapp.generated.resources.ph_new_password
import rollin_up.composeapp.generated.resources.ph_reenter_new_password

@Composable
fun ResetPasswordForm(
    onNavigateUp: () -> Unit,
    uiState: ResetPasswordUiState,
    cb: ResetPasswordCallback,
) {
    when (uiState.step) {
        0 -> {
            SubmitEmailForm(
                uiState = uiState,
                onSubmitEmail = cb.onSubmitEmail,
                onNavigateUp = onNavigateUp
            )
        }

        1 -> {
            SubmitOtpForm(
                onSubmitOtp = cb.onSubmitOtp,
                uiState = uiState,
                onUpdateStep = cb.onUpdateStep,
                onResendOtp = cb.onSubmitEmail
            )
        }

        else -> {
            NewPasswordForm(
                uiState = uiState,
                onSubmitNewPassword = cb.onSubmitNewPassword
            )
        }

    }
}

@Composable
fun SubmitEmailForm(
    uiState: ResetPasswordUiState,
    onSubmitEmail: (String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    var emailInput by remember { mutableStateOf("") }
    var emailError: ResetPasswordErrorType? by remember { mutableStateOf(null) }

    LaunchedEffect(uiState.step) {
        emailInput = ""
        emailError = null
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenPaddingValues)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(256.dp)
        ) {
            TextField(
                title = stringResource(Res.string.label_email),
                value = emailInput,
                onValueChange = { value ->
                    emailInput = value
                    if (value.contains(" ")) {
                        emailError = ResetPasswordErrorType.EMAIL_CONTAIN_SPACE
                    }
                    if (value.isBlank()) {
                        emailError = null
                    }
                    emailError = null
                },
                placeholder = stringResource(Res.string.ph_email),
                isError = emailError != null,
                errorMsg = emailError?.getMessage(),
                leadingIcon = Res.drawable.ic_mail_line_24,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Button(
                text = stringResource(Res.string.label_submit),
                onClick = {
                    if (emailInput.isBlank()) {
                        emailError = ResetPasswordErrorType.EMAIL_EMPTY
                    } else {
                        onSubmitEmail(emailInput)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(itemGap8)
        Text(
            text = stringResource(Res.string.label_back_to_login),
            color = theme.textPrimary,
            style = Style.title,
            modifier = Modifier
                .clickable {
                    onNavigateUp()
                }
                .padding(itemGap4)
        )
    }
}

@Composable
private fun NewPasswordForm(
    uiState: ResetPasswordUiState,
    onSubmitNewPassword: (String) -> Unit,
) {
    var firstPassword: String by remember { mutableStateOf("") }
    var firstPasswordError: PasswordErrorType? by remember { mutableStateOf(null) }
    var secondPassword: String by remember { mutableStateOf("") }
    var secondPasswordError: PasswordErrorType? by remember { mutableStateOf(null) }
    var isBothError: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.step) {
        firstPassword = ""
        firstPasswordError = null
        secondPassword = ""
        secondPasswordError = null
        isBothError = false

    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(screenPaddingValues)
            .height(256.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PasswordTextField(
                value = firstPassword,
                onValueChange = { value ->
                    firstPasswordError = null
                    isBothError = false
                    val errorMessage = Utils.validatePassword(value)
                    firstPassword = value
                    firstPasswordError = errorMessage
                },
                title = stringResource(Res.string.label_new_password),
                placeholder = stringResource(Res.string.ph_new_password),
                isError = firstPasswordError != null || isBothError,
                errorMsg = firstPasswordError?.getMessage()
            )
            Spacer(itemGap4)
            PasswordTextField(
                value = secondPassword,
                onValueChange = { value ->
                    secondPasswordError = null
                    isBothError = false
                    val errorMessage = Utils.validatePassword(value)
                    secondPassword = value
                    secondPasswordError = errorMessage
                },
                title = stringResource(Res.string.label_repeat_new_password),
                placeholder = stringResource(Res.string.ph_reenter_new_password),
                isError = secondPasswordError != null || isBothError,
                errorMsg = secondPasswordError?.getMessage()
            )
        }
        Button(
            onClick = {
                if (firstPassword.isBlank()) {
                    firstPasswordError = PasswordErrorType.EMPTY
                    return@Button
                }
                if (secondPassword.isBlank()) {
                    secondPasswordError = PasswordErrorType.EMPTY
                    return@Button
                }
                if (firstPassword != secondPassword) {
                    isBothError = true
                    secondPasswordError = PasswordErrorType.NOT_MATCH
                    return@Button
                }
                if (
                    listOf(
                        firstPasswordError,
                        secondPasswordError
                    ).all { it != null } || !isBothError
                ) {
                    onSubmitNewPassword(firstPassword)
                }
            },
            text = stringResource(Res.string.label_submit),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SubmitOtpForm(
    onSubmitOtp: (String) -> Unit,
    uiState: ResetPasswordUiState,
    onUpdateStep: (Int) -> Unit,
    onResendOtp: (String) -> Unit,
) {
    var otpInput: String by remember { mutableStateOf("") }
    var otpError: ResetPasswordErrorType? by remember { mutableStateOf(null) }
    val countdown = LocalTime.fromSecondOfDay(uiState.otpCountdown).toString()

    LaunchedEffect(uiState.step) {
        otpInput = ""
        otpError = null
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
        ) {
            OTPTextField(
                value = otpInput,
                onValueChange = { value ->
                    otpInput = value
                    otpError = null
                },
                isError = otpError != null,
                textError = otpError?.getMessage()
            )
            Button(
                text = stringResource(Res.string.label_submit),
                onClick = {
                    if (otpInput.isBlank()) {
                        otpError = ResetPasswordErrorType.OTP_EMPTY
                    } else if (otpInput.length < 5) {
                        otpError = ResetPasswordErrorType.OTP_INVALID
                    } else {
                        onSubmitOtp(otpInput)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (uiState.otpStatus.email.isNotBlank()) {
            Spacer(14.dp)
            Text(
                text = stringResource(Res.string.msg_otp_already_sent, uiState.otpStatus.email),
                color = theme.textPrimary,
                textAlign = TextAlign.Center,
                style = Style.body,
            )
            Spacer(itemGap4)
            if (uiState.otpCountdown > 0L) {
                TimeText(
                    value = LocalTime.fromSecondOfDay(uiState.otpCountdown),
                    style = Style.body
                )
            } else {
                Text(
                    text = stringResource(Res.string.label_resend_otp),
                    color = theme.textPrimary,
                    style = Style.title,
                    modifier = Modifier
                        .clickable {
                            onResendOtp(uiState.email)
                        }
                )
            }
        }
        Spacer(itemGap8)
        Text(
            text = stringResource(Res.string.label_reenter_email),
            color = theme.textPrimary,
            style = Style.title,
            modifier = Modifier
                .clickable {
                    onUpdateStep(0)
                }
                .padding(horizontal = itemGap4)
        )
    }
}
