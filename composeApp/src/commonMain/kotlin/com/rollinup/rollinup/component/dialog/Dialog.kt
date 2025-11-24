package com.rollinup.rollinup.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenWidth
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24

@Composable
fun Dialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    )

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismissRequest(false) },
            properties = properties,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = theme.popUpBg,
                        shape = RoundedCornerShape(screenPadding)
                    )
                    .padding(contentPadding)
                    .then(modifier)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onDismissRequest(false)
                            }
                            .size(32.dp),
                        tint = theme.lineStroke,
                        painter = painterResource(Res.drawable.ic_close_line_24),
                        contentDescription = null
                    )
                }
                content()
            }
        }
    }

}

@Composable
fun AlertDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    content: AnnotatedString,
    title: String? = null,
    showCancelButton: Boolean = true,
    isSingleButton: Boolean = false,
    btnConfirmText: String = "Confirm",
    btnCancelText: String = "Close",
    icon: DrawableResource? = null,
    iconTint: Color = theme.primary,
    severity: Severity = Severity.PRIMARY,
) {
    AlertDialog(
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        onClickConfirm = onClickConfirm,
        onClickCancel = onClickCancel,
        title = title,
        showCancelButton = showCancelButton,
        isSingleButton = isSingleButton,
        btnConfirmText = btnConfirmText,
        btnCancelText = btnCancelText,
        icon = icon,
        iconTint = iconTint,
        severity = severity
    ) {
        Text(
            text = content,
            color = theme.bodyText,
            style = Style.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AlertDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    content: String,
    title: String? = null,
    showCancelButton: Boolean = true,
    isSingleButton: Boolean = false,
    btnConfirmText: String = "Confirm",
    btnCancelText: String = "Close",
    icon: DrawableResource? = null,
    iconTint: Color = theme.primary,
    severity: Severity = Severity.PRIMARY,
) {
    AlertDialog(
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        onClickConfirm = onClickConfirm,
        onClickCancel = onClickCancel,
        title = title,
        showCancelButton = showCancelButton,
        isSingleButton = isSingleButton,
        btnConfirmText = btnConfirmText,
        btnCancelText = btnCancelText,
        icon = icon,
        iconTint = iconTint,
        severity = severity
    ) {
        Text(
            text = content,
            color = theme.bodyText,
            style = Style.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AlertDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String? = null,
    showCancelButton: Boolean = true,
    isSingleButton: Boolean = false,
    btnConfirmText: String = "Confirm",
    btnCancelText: String = "Close",
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    icon: DrawableResource? = null,
    iconTint: Color = theme.primary,
    severity: Severity = Severity.PRIMARY,
    content: @Composable () -> Unit,
) {
    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = true
    )
    val dialogWidth = if (getPlatform().isMobile()) getScreenWidth() * 0.75f else 400.dp

    if (isShowDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest(false)
            },
            properties = properties,
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .width(dialogWidth)
                    .background(color = theme.popUpBg),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(itemGap8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    icon?.let {
                        Icon(
                            painter = painterResource(it),
                            contentDescription = null,
                            modifier = Modifier.size(
                                24.dp
                            ),
                            tint = iconTint
                        )
                    }
                    title?.let {
                        Text(
                            text = it,
                            color = theme.bodyText,
                            style = Style.popupTitle
                        )
                    }
                    content()
                    Spacer(itemGap8)
                    AlertDialogButton(
                        onClickConfirm = {
                            onClickConfirm()
                            onDismissRequest(false)
                        },
                        onClickCancel = {
                            onClickCancel()
                            onDismissRequest(false)
                        },
                        btnCancelText = btnCancelText,
                        btnConfirmText = btnConfirmText,
                        isSingleButton = isSingleButton,
                        showCancelButton = showCancelButton,
                        severity = severity
                    )

                }
            }
        }
    }

}


@Composable
private fun AlertDialogButton(
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    btnCancelText: String,
    btnConfirmText: String,
    isSingleButton: Boolean,
    showCancelButton: Boolean,
    severity: Severity,
) {
    val showCloseButtonState = !isSingleButton || (isSingleButton && showCancelButton)
    val showConfirmButtonState = !isSingleButton || (isSingleButton && !showCancelButton)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(itemGap8)
    ) {
        if (showCloseButtonState) {
            Button(
                modifier = Modifier.weight(1f),
                text = btnCancelText,
                onClick = onClickCancel,
                severity = severity,
                type = ButtonType.OUTLINED
            )
        }
        if (showConfirmButtonState) {
            Button(
                modifier = Modifier.weight(1f),
                text = btnConfirmText,
                onClick = onClickConfirm,
                severity = severity,
                type = ButtonType.FILLED
            )
        }
    }
}
