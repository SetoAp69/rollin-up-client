package com.rollinup.rollinup.component.bottomsheet

import SnackBarHost
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.theme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_apply

/**
 * A wrapper around Material 3 [ModalBottomSheet] that provides built-in SnackBar support.
 *
 * This component manages the sheet state and overlays a [SnackBarHost] on top of the sheet content,
 * allowing the content to trigger snackbars that appear specifically within the sheet's context.
 *
 * @param onDismissRequest Callback invoked when the user requests to dismiss the bottom sheet.
 * @param modifier The modifier to be applied to the content layout.
 * @param isShowSheet Controls the visibility of the bottom sheet.
 * @param skipPartialExpanded Whether the sheet should skip the partially expanded state and go directly to full expansion.
 * @param containerColor The background color of the bottom sheet.
 * @param dragHandleColor The color of the custom drag handle indicator.
 * @param content The content of the bottom sheet. Provides a callback [OnShowSnackBar] to trigger snackbar messages.
 */
@Composable
fun BottomSheet(
    onDismissRequest: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isShowSheet: Boolean,
    skipPartialExpanded: Boolean = true,
    containerColor: Color = theme.popUpBg,
    dragHandleColor: Color = theme.textFieldBackGround,
    content: @Composable ColumnScope.(OnShowSnackBar) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartialExpanded,
        confirmValueChange = { sheetValue ->
            true
        },
    )
    val shape = BottomSheetDefaults.ExpandedShape
    val properties = ModalBottomSheetProperties(shouldDismissOnBackPress = true)
    val scope = rememberCoroutineScope()
    var isSuccess by remember { mutableStateOf(false) }

    if (isShowSheet) {
        val snackBarHostState = remember { SnackbarHostState() }

        fun showSnackBar(message: String, success: Boolean) {
            isSuccess = success
            scope.launch {
                snackBarHostState.showSnackbar(message = message)
            }
        }

        Box(contentAlignment = Alignment.BottomCenter) {
            ModalBottomSheet(
                onDismissRequest = {
                    onDismissRequest(
                        false
                    )
                },
                sheetState = sheetState,
                shape = shape,
                containerColor = containerColor,
                contentColor = theme.bodyText,
                tonalElevation = 0.dp,
                contentWindowInsets = { BottomSheetDefaults.windowInsets },
                properties = properties,
                content = {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Column(
                            modifier = modifier
                                .padding(
                                    bottom = 12.dp
                                )
                        ) {
                            content { message, success ->
                                showSnackBar(message, success)
                            }
                        }
                    }
                },
                dragHandle = {
                    BottomSheetDragHandle(color = dragHandleColor)
                },
                scrimColor = BottomSheetDefaults.ScrimColor.copy(alpha = 0.7f)
            )
            SnackBarHost(
                snackBarHostState = snackBarHostState,
                isSuccess = isSuccess,
            )
        }

    }
}

/**
 * A variant of [BottomSheet] that includes a primary action button at the bottom.
 *
 * This composable wraps the standard [BottomSheet] and appends a button below the content,
 * typically used for "Apply" or "Confirm" actions.
 *
 * @param onDismissRequest Callback invoked when the user requests to dismiss the bottom sheet.
 * @param modifier The modifier to be applied to the content layout.
 * @param isShowSheet Controls the visibility of the bottom sheet.
 * @param onClickConfirm Callback invoked when the confirm button is clicked.
 * @param btnConfirmText The text displayed on the confirm button.
 * @param skipPartialExpanded Whether the sheet should skip the partially expanded state.
 * @param dragHandleColor The color of the custom drag handle indicator.
 * @param containerColor The background color of the bottom sheet.
 * @param content The content of the bottom sheet.
 */
@Composable
fun BottomSheet(
    onDismissRequest: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isShowSheet: Boolean,
    onClickConfirm: () -> Unit,
    btnConfirmText: String = stringResource(Res.string.label_apply),
    skipPartialExpanded: Boolean = true,
    dragHandleColor: Color = theme.textFieldBackGround,
    containerColor: Color = theme.popUpBg,
    content: @Composable ColumnScope.() -> Unit,
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        isShowSheet = isShowSheet,
        skipPartialExpanded = skipPartialExpanded,
        containerColor = containerColor,
        dragHandleColor = dragHandleColor
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
            Spacer(itemGap8)
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                text = btnConfirmText,
                onClick = {
                    onClickConfirm()
                    onDismissRequest(!isShowSheet)
                }
            )
        }


    }

}

/**
 * A custom visual indicator for the drag handle of the Bottom Sheet.
 *
 * @param color The background color of the drag handle.
 */
@Composable
private fun BottomSheetDragHandle(
    color: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .height(4.dp)
                .width(48.dp)
        ) {}
    }
}