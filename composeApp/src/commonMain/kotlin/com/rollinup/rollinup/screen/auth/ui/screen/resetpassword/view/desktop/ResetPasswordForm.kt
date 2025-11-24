package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.desktop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.PasswordTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.Utils
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordCallback
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.OTPTextField
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_mail_line_24

@Composable
internal fun ResetPasswordFormDesktop(
    onNavigateUp: () -> Unit,
    uiState: ResetPasswordUiState,
    cb: ResetPasswordCallback,
) {
    when (uiState.step) {
        0 -> SubmitEmailForm(
            onSubmitEmail = cb.onSubmitEmail,
            uiState = uiState,
            onNavigateUp = onNavigateUp
        )

        1 -> SubmitOtpForm(
            onSubmitOtp = cb.onSubmitOtp,
            uiState = uiState,
            onUpdateStep = cb.onUpdateStep
        )

        else -> SubmitNewPasswordForm(
            uiState = uiState,
            onSubmitNewPassword = cb.onSubmitNewPassword
        )
    }
}


@Composable
private fun SubmitEmailForm(
    onSubmitEmail: (String) -> Unit,
    uiState: ResetPasswordUiState,
    onNavigateUp: () -> Unit,
) {
    var emailInput by remember { mutableStateOf("") }
    var emailError: String? by remember { mutableStateOf(null) }

    LaunchedEffect(uiState.step) {
        emailInput = ""
        emailError = null
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = emailInput,
            onValueChange = { value ->
                emailInput = value
                if (value.contains(" ")) {
                    emailError = "Email or Username cannot contain spaces"
                }
                if (value.length < 5) {
                    emailError = "Email or Username cannot be less than 5 characters"
                }
                if (value.isBlank()) {
                    emailError = null
                }
                emailError = null

            },
            placeholder = "Enter your email or username",
            title = "Email or username",
            isError = emailError != null,
            errorMsg = emailError,
            leadingIcon = Res.drawable.ic_mail_line_24,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        Spacer(itemGap8)
        Button(
            onClick = {
                if (emailInput.isNotBlank()) {
                    onSubmitEmail(emailInput)
                } else {
                    emailError = "Email or username can't be empty"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            text = "Submit"
        )
        Spacer(itemGap8)
        Text(
            text = "Back to Login",
            style = Style.title,
            color = theme.textPrimary,
            modifier = Modifier
                .clickable {
                    onNavigateUp()
                }
                .padding(itemGap4)
        )
    }
}

@Composable
private fun SubmitOtpForm(
    onSubmitOtp: (String) -> Unit,
    uiState: ResetPasswordUiState,
    onUpdateStep: (Int) -> Unit,
) {
    var otpInput: String by remember { mutableStateOf("") }
    var otpError: String? by remember { mutableStateOf(null) }

    LaunchedEffect(uiState.step) {
        otpInput = ""
        otpError = null
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OTPTextField(
            value = otpInput,
            onValueChange = {
                otpInput = it
                otpError = null
            },
            isError = otpError != null,
            textError = otpError
        )
        Spacer(itemGap8)
        Button(
            onClick = {
                if (otpInput.isBlank()) {
                    otpError = "OTP cannot be empty"
                } else if (otpInput.length < 5) {
                    otpError = "You need to fills all the digits"
                } else {
                    onSubmitOtp(otpInput)
                }
            },
            text = "Submit"
        )
        Spacer(itemGap8)
        uiState.actualEmail?.let {
            Spacer(itemGap4)
            Text(
                text = "We've already sent an OTP code to $it. You can request new otp after 5 minutes.",
                color = theme.bodyText,
                textAlign = TextAlign.Center,
                style = Style.body,
            )
        }
        Spacer(itemGap8)
        Text(
            text = "Re-enter email/username",
            color = theme.textPrimary,
            style = Style.title,
            modifier = Modifier
                .clickable {
                    onUpdateStep(0)
                }
                .padding(itemGap4)
        )
    }
}

@Composable
private fun SubmitNewPasswordForm(
    uiState: ResetPasswordUiState,
    onSubmitNewPassword: (String) -> Unit,
) {
    var firstPassword: String by remember { mutableStateOf("") }
    var firstPasswordError: String? by remember { mutableStateOf(null) }
    var secondPassword: String by remember { mutableStateOf("") }
    var secondPasswordError: String? by remember { mutableStateOf(null) }
    var isBothError: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.step) {
        firstPassword = ""
        firstPasswordError = null
        secondPassword = ""
        secondPasswordError = null
        isBothError = false

    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
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
            title = "New Password",
            placeholder = "Enter new password",
            isError = firstPasswordError != null || isBothError,
            errorMsg = firstPasswordError
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
            title = "Repeat New Password",
            placeholder = "Re-Enter new password",
            isError = secondPasswordError != null || isBothError,
            errorMsg = secondPasswordError
        )
        Spacer(itemGap8)
        Button(
            onClick = {
                if (firstPassword.isBlank()) {
                    firstPasswordError = "Password cannot be empty"
                    return@Button
                }
                if (secondPassword.isBlank()) {
                    secondPasswordError = "Password cannot be empty"
                    return@Button
                }
                if (firstPassword != secondPassword) {
                    isBothError = true
                    secondPasswordError = "Password does not match"
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
            text = "Submit",
            modifier = Modifier.fillMaxWidth()
        )
    }
}