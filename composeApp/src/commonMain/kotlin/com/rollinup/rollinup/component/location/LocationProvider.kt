package com.rollinup.rollinup.component.location

import androidx.compose.runtime.Composable
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import dev.jordond.compass.Location
import dev.jordond.compass.geolocation.Geolocator
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_permission_denied
import rollin_up.composeapp.generated.resources.msg_location_permission_denied

@Composable
expect fun LocationHandler(
    onLocationChanges: (Location?) -> Unit,
    startTracking: Boolean,
)

expect fun getLocator(): Geolocator?

@Composable
fun LocationPermissionDeniedDialog(
    onDismissRequest: (Boolean) -> Unit,
    isShowDialog: Boolean,
    onClickConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.danger,
        title = stringResource(Res.string.label_permission_denied),
        content = stringResource(Res.string.msg_location_permission_denied),
        severity = Severity.DANGER,
        onClickCancel = {
            onCancel()
            onDismissRequest(false)
        },
        onClickConfirm = {
            onClickConfirm()
            onDismissRequest(false)
        }
    )
}