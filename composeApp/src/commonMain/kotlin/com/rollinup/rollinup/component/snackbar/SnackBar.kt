import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.theme

/**
 * A container for displaying Snackbars within a specific layout area (usually bottom center).
 *
 * @param snackBarHostState The state object controlling the snackbar queue.
 * @param isSuccess Determines the visual styling (success vs error) of the displayed snackbar.
 */
@Composable
fun SnackBarHost(
    snackBarHostState: SnackbarHostState,
    isSuccess: Boolean,
) {
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackBar(
                snackBarData = it,
                isSuccess = isSuccess,
            )
        }
    }
}

/**
 * A customized Snackbar composable.
 *
 * Applies specific color schemes based on the [isSuccess] flag (green/success vs red/danger).
 *
 * @param snackBarData Data provided by the host (message, action label, etc.).
 * @param isSuccess Controls the color theme.
 * @param cornerRad The corner radius of the snackbar.
 * @param modifier Modifier applied to the snackbar.
 */
@Composable
fun SnackBar(
    snackBarData: SnackbarData,
    isSuccess: Boolean,
    cornerRad: Dp = 12.dp,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSuccess) theme.chipSuccessBg else theme.danger50
    val contentColor = if (isSuccess) theme.chipSuccessContent else theme.chipDangerContent
    val shape = RoundedCornerShape(cornerRad)

    Snackbar(
        modifier = modifier,
        snackbarData = snackBarData,
        actionOnNewLine = true,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
    )
}