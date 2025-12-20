package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.desktop

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
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordCallback
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_account_recovery

@Composable
fun ResetPasswordDesktopContent(
    onNavigateUp: () -> Unit,
    uiState: ResetPasswordUiState,
    cb: ResetPasswordCallback,
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
                        .padding(24.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.label_account_recovery),
                        color = theme.bodyText,
                        style = Style.headerBold
                    )
                    ResetPasswordFormDesktop(
                        onNavigateUp = onNavigateUp,
                        uiState = uiState,
                        cb = cb
                    )
                }
            }
        }
    }
}

