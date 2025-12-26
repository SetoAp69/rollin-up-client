package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordCallback
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordStep
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.UpdatePasswordUiState

@Composable
actual fun UpdatePasswordContent(
    cb: UpdatePasswordCallback,
    uiState: UpdatePasswordUiState,
    onShowSnackBar: OnShowSnackBar,
) {

    Scaffold(
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .width(500.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState.currentStep) {
                        UpdatePasswordStep.OTP -> {
                            SubmitOtpForm(
                                uiState = uiState,
                                onRequestOtp = cb.onResendOtp,
                                cb = cb,
                            )
                        }

                        UpdatePasswordStep.UPDATE_PASSWORD -> {
                            Text(
                                text = "Update Password",
                                color = theme.bodyText,
                                style = Style.headerBold
                            )
                            UpdatePasswordForm(
                                formData = uiState.formData,
                                onUpdateFormData = cb.onUpdateFormData,
                                onSubmit = cb.onSubmit
                            )
                        }
                    }
                }
            }
        }
    }
}