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
    val snackBarHostState = remember { SnackbarHostState() }
    var isSuccess by remember { mutableStateOf(false) }

    fun showSnackBar(message: String, success: Boolean) {
        isSuccess = success
        scope.launch {
            snackBarHostState.showSnackbar(message = message)
        }
    }

    if (isShowSheet) {
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
                    Box(contentAlignment = Alignment.BottomCenter){
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


@Composable
fun BottomSheet(
    onDismissRequest: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isShowSheet: Boolean,
    onClickConfirm: () -> Unit,
    btnConfirmText: String = "Apply",
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