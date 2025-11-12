import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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