package com.rollinup.rollinup.component.platformwarning

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.Role
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.model.getLabel
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.toAnnotatedString
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_close
import rollin_up.composeapp.generated.resources.label_access_denied
import rollin_up.composeapp.generated.resources.msg_invalid_platform_error

/**
 * A blocking warning screen displayed when a user attempts to log in with a role that is restricted
 * on the current platform (e.g., trying to access a mobile-only view from a web desktop environment).
 *
 * This component locks the UI with a full-screen [Scaffold] containing a non-dismissible [AlertDialog]
 * that prompts the user to logout.
 *
 * @param role The user's role (e.g., Admin, Driver).
 * @param platform The current platform (e.g., Android, Desktop).
 */
@Composable
fun PlatformWarning(
    role: Role,
    platform: Platform,
) {
    val auth = LocalAuthViewmodel.current

    Scaffold {
        AlertDialog(
            isShowDialog = true,
            title = stringResource(Res.string.label_access_denied),
            icon = Res.drawable.ic_info_line_24,
            iconTint = theme.danger,
            onDismissRequest = {},
            content = stringResource(Res.string.msg_invalid_platform_error, role.getLabel(), platform.name).toAnnotatedString(),
            btnCancelText = stringResource(Res.string.label_close),
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