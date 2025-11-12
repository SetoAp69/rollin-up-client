package com.rollinup.rollinup.component.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun ToolTip(
    message: String,
    content: @Composable () -> Unit,
) {
    val toolTipPositionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
    val toolTipState = rememberTooltipState()

    TooltipBox(
        positionProvider = toolTipPositionProvider,
        tooltip = {
            if (message.isNotBlank()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            color = theme.chipSecondaryBg,
                            shape = TooltipDefaults.plainTooltipContainerShape
                        )
                        .padding(itemGap4)

                ) {
                    Text(
                        text = message,
                        color = theme.chipSecondaryContent,
                        style = Style.small
                    )
                }
                Spacer(modifier = Modifier.height(itemGap4))
            }
        },
        state = toolTipState,
        focusable = false,
        enableUserInput = true,
    ) {
        content()
    }
}