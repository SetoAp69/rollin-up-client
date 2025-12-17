package com.rollinup.rollinup.component.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun TableFilterRow(
    onReset: () -> Unit,
    showReset: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showReset) {
            Text(
                text = "Reset",
                style = Style.title,
                color = theme.textPrimary,
                modifier = Modifier
                    .clickable {
                        onReset()
                    }
                    .padding(bottom = 12.dp)
            )
        }
        content()
    }
}