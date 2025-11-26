package com.rollinup.rollinup.component.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileInfoField(
    title: String,
    icon: DrawableResource,
    value: String,
) {
    TextFieldTitle(
        title = title
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(icon),
                tint = theme.primary,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(itemGap8)
            Text(
                text = value,
                color = theme.bodyText,
                style = Style.body
            )

        }
        HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
    }
}