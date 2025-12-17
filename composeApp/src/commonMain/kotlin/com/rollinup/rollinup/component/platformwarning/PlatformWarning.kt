package com.rollinup.rollinup.component.platformwarning

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.Role
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.toAnnotatedString
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24

@Composable
fun PlatformWarning(
    role: Role,
    platform: Platform,
) {
    val auth = LocalAuthViewmodel.current


    Scaffold {
        AlertDialog(
            isShowDialog = true,
            title = "Access Denied",
            icon = Res.drawable.ic_info_line_24,
            iconTint = theme.danger,
            onDismissRequest = {},
            content = "Account with **$role** role have no access on **$platform** platform, please login using another type of platform".toAnnotatedString(),
            btnCancelText = "Close",
            severity = Severity.DANGER,
            isSingleButton = true,
            showCancelButton = true,
            onClickCancel = {
                auth.logout()
            },
            onClickConfirm = {},
        )
    }

}