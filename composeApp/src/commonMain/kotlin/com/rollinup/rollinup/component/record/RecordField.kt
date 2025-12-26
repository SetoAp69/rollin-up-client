package com.rollinup.rollinup.component.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact

@Composable
fun RecordField(
    title: String,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier.padding(top = itemGap4),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(itemGap4)
    ) {
        Text(
            text = title,
            style = Style.body,
            color = theme.bodyText,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .width(if (isCompact) 100.dp else 116.dp)
        )
        Text(
            text = ":",
            style = Style.body,
            color = theme.bodyText,
            textAlign = TextAlign.Start,
        )
        content()
    }
}


@Composable
fun RecordField(
    title: String,
    content: String,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
) {
    Row(
        modifier = Modifier.padding(top = itemGap4),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(itemGap4)
    ) {
        Text(
            text = title,
            style = Style.body,
            color = theme.bodyText,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .width(if (isCompact) 100.dp else 116.dp)
        )
        Text(
            text = ":",
            style = Style.body,
            color = theme.bodyText,
            textAlign = TextAlign.Start,
        )
        Text(
            text = content.ifBlank { "-" },
            style = Style.body,
            color = theme.bodyText,
            textAlign = TextAlign.Start,
        )
    }
}