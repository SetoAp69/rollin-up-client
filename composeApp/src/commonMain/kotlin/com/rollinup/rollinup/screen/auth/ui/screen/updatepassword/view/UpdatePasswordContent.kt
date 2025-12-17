package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.textfield.PasswordTextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.Utils
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordCallback
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordFormData
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.OTPTextField
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.UpdatePasswordUiState
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalTime

@Composable
expect fun UpdatePasswordContent(
    cb: UpdatePasswordCallback,
    uiState: UpdatePasswordUiState,
    onShowSnackBar: OnShowSnackBar,
)

@Composable
fun SubmitOtpForm(
    cb: UpdatePasswordCallback,
    uiState: UpdatePasswordUiState,
    onRequestOtp: () -> Unit,
) {
    var timeInSecond by remember { mutableStateOf(LocalTime(0, 1, 20).toSecondOfDay()) }

    LaunchedEffect(uiState.currentStep) {
        cb.onResetForm()
    }

    LaunchedEffect(uiState.startTimer) {
        if (uiState.startTimer) {
            while (timeInSecond > 0) {
                delay(1000)
                timeInSecond -=1
            }
        }
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
                value = uiState.otp,
                onValueChange = { value ->
                    cb.onUpdateOtp(value)
                },
                isError = uiState.otpError != null,
                textError = uiState.otpError
            )
            Button(
                text = "Submit",
                onClick = {
                    cb.onSubmitOtp(uiState.otp)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(14.dp)
        Text(
            text = "We've already sent an OTP code to your email. You can request new otp after 2 minutes.",
            color = theme.textPrimary,
            textAlign = TextAlign.Center,
            style = Style.body,
        )
        Spacer(itemGap8)
        if (timeInSecond != 0) {
            val time = LocalTime.fromSecondOfDay(timeInSecond)
            Text(
                text = DateFormatter.formatTimeMinuteSecond(time),
                style = Style.body,
                color = theme.textPrimary
            )
        } else {
            Text(
                text = "Re-send OTP",
                color = theme.textPrimary,
                style = Style.title,
                modifier = Modifier
                    .clickable {
                        onRequestOtp()
                    }
                    .padding(horizontal = itemGap4)
            )
        }
    }
}

@Composable
fun UpdatePasswordForm(
    formData: UpdatePasswordFormData,
    onUpdateFormData: (UpdatePasswordFormData) -> Unit,
    onSubmit: (UpdatePasswordFormData) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(screenPaddingValues)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(256.dp)
        ) {
            PasswordTextField(
                value = formData.passwordOne,
                onValueChange = { value ->
                    val errorMessage = Utils.validatePassword(value)
                    onUpdateFormData(
                        formData.copy(
                            passwordOne = value,
                            passwordOneError = errorMessage
                        )
                    )
                },
                title = "New Password",
                placeholder = "Enter new password",
                isError = formData.passwordOneError != null,
                errorMsg = formData.passwordOneError
            )
            Spacer(itemGap4)
            PasswordTextField(
                value = formData.passwordTwo,
                onValueChange = { value ->
                    val errorMessage = Utils.validatePassword(value)
                    onUpdateFormData(
                        formData.copy(
                            passwordTwo = value,
                            passwordTwoError = errorMessage
                        )
                    )
                },
                title = "Repeat New Password",
                placeholder = "Re-Enter new password",
                isError = formData.passwordTwoError != null,
                errorMsg = formData.passwordTwoError
            )
        }
        Button(
            onClick = {
                onSubmit(formData)
            },
            text = "Submit",
            modifier = Modifier.fillMaxWidth()
        )
    }
}


