package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.header.Header
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordCallback
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_account_recovery

@Composable
fun ResetPasswordMobileContent(
    onNavigateUp: () -> Unit,
    uiState: ResetPasswordUiState,
    cb: ResetPasswordCallback,
) {
    Scaffold(
        topBar = {
            Header(stringResource(Res.string.label_account_recovery))
        },
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(96.dp)
            ResetPasswordForm(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                cb = cb
            )

        }
    }
}