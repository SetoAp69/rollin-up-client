package com.rollinup.rollinup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.topbar.BaseTopBar
import com.rollinup.rollinup.component.topbar.TopAppBarDefaults
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_down_24
import rollin_up.composeapp.generated.resources.ic_exit_line_24

@Composable
fun NavHostTopBar(
    userData: LoginEntity,
    onLogOut: () -> Unit,
) {
    val title =
        if (userData.role == Role.ADMIN) "Rollin up - Admin Console" else "Rollin up - Client"
    BaseTopBar(
        title = title,
        showNavigateUp = false,
        color = TopAppBarDefaults.topAppBarColors.copy(containerColor = theme.popUpBg),
        actionContent = {
            NavHostTopBarContent(
                userData = userData,
                onLogOut = onLogOut
            )
        },
    )
}

@Composable
private fun NavHostTopBarContent(
    userData: LoginEntity,
    onLogOut: () -> Unit,
) {
    var showDropDown by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Hello",
                color = theme.bodyText,
                style = Style.body
            )
            Spacer(2.dp)
            Text(
                text = userData.fullName,
                color = theme.bodyText,
                style = Style.popupTitle
            )
        }
        Spacer(itemGap4)
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = theme.primary,
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userData.firstName.take(1).uppercase().ifBlank { "-" },
                style = Style.title,
                color = Color.White
            )
        }
        Spacer(itemGap4)
        Box {
            IconButton(
                icon = Res.drawable.ic_drop_down_arrow_line_down_24,
                severity = Severity.PRIMARY,
                size = 16.dp,
                onClick = {
                    showDropDown = true
                }
            )
            DropDownMenu(
                isShowDropDown = showDropDown,
                onDismissRequest = { showDropDown = it },
            ) {
                DropDownMenuItem(
                    label = "Logout",
                    icon = Res.drawable.ic_exit_line_24,
                    onClick = onLogOut
                )
            }
        }
    }
}