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

/**
 * Platform-specific composable to handle location updates.
 *
 * Implementations should manage permission requests and start/stop tracking based on [startTracking].
 *
 * @param onLocationChanges Callback invoked with the new [Location] or null if unavailable.
 * @param startTracking Whether to actively track location.
 */
@Composable
expect fun LocationHandler(
    onLocationChanges: (Location?) -> Unit,
    startTracking: Boolean,
)

/**
 * Platform-specific function to retrieve the Compass [Geolocator] instance.
 *
 * @return The Geolocator instance or null if not available.
 */
expect fun getLocator(): Geolocator?

/**
 * A dialog displayed when location permissions are denied.
 *
 * @param onDismissRequest Callback to dismiss the dialog.
 * @param isShowDialog Controls visibility.
 * @param onClickConfirm Action for the confirm button (e.g., open settings).
 * @param onCancel Action for the cancel button.
 */
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