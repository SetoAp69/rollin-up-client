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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.textfield.PasswordTextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.time.TimeText
import com.rollinup.rollinup.component.utils.Utils
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordCallback
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordFormData
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.OTPTextField
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.VerifyAccountUiState
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_new_password
import rollin_up.composeapp.generated.resources.label_repeat_new_password
import rollin_up.composeapp.generated.resources.label_resend_otp
import rollin_up.composeapp.generated.resources.label_submit
import rollin_up.composeapp.generated.resources.msg_otp_already_sent
import rollin_up.composeapp.generated.resources.ph_new_password
import rollin_up.composeapp.generated.resources.ph_reenter_new_password

@Composable
expect fun UpdatePasswordContent(
    cb: UpdatePasswordCallback,
    uiState: VerifyAccountUiState,
    onShowSnackBar: OnShowSnackBar,
)

@Composable
fun SubmitOtpForm(
    cb: UpdatePasswordCallback,
    uiState: VerifyAccountUiState,
    onRequestOtp: () -> Unit,
) {
    LaunchedEffect(uiState.currentStep) {
        cb.onResetOtp()
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
                textError = uiState.otpError?.getMessage()
            )
            Button(
                text = stringResource(Res.string.label_submit),
                onClick = {
                    cb.onSubmitOtp(uiState.otp)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(14.dp)
        Text(
            text = stringResource(Res.string.msg_otp_already_sent),
            color = theme.textPrimary,
            textAlign = TextAlign.Center,
            style = Style.body,
        )
        Spacer(itemGap8)

        if (uiState.countdown > 0) {
            val countdown = LocalTime.fromSecondOfDay(uiState.countdown.toInt())
            TimeText(
                value = countdown,
                style = Style.body
            )
        } else {
            Text(
                text = stringResource(Res.string.label_resend_otp),
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
                title = stringResource(Res.string.label_new_password),
                placeholder = stringResource(Res.string.ph_new_password),
                isError = formData.passwordOneError != null,
                errorMsg = formData.passwordOneError?.getMessage()
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
                title = stringResource(Res.string.label_repeat_new_password),
                placeholder = stringResource(Res.string.ph_reenter_new_password),
                isError = formData.passwordTwoError != null,
                errorMsg = formData.passwordTwoError?.getMessage()
            )
        }
        Button(
            onClick = {
                onSubmit(formData)
            },
            text = stringResource(Res.string.label_submit),
            modifier = Modifier.fillMaxWidth()
        )
    }

}


