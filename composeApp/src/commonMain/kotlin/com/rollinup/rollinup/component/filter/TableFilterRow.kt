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
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_reset

/**
 * A layout container for filter controls typically placed above a data table.
 *
 * It arranges filter inputs horizontally and optionally includes a "Reset" button
 * aligned to the bottom (to match baseline with inputs).
 *
 * @param onReset Callback triggered when the reset text is clicked.
 * @param showReset Controls visibility of the reset option.
 * @param content The filter components to display in the row.
 */
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
                text = stringResource(Res.string.label_reset),
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