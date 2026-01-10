package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.header.Header
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordCallback
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordStep
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.VerifyAccountUiState

@Composable
actual fun UpdatePasswordContent(
    cb: UpdatePasswordCallback,
    uiState: VerifyAccountUiState,
    onShowSnackBar: OnShowSnackBar,
) {
    Scaffold(
        showLoadingOverlay = uiState.isLoadingOverlay,
        topBar = {
            Header("Update Password")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                    Spacer(96.dp)
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
