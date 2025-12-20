package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.topbar.BaseTopBar
import com.rollinup.rollinup.component.topbar.TopAppBarDefaults
import com.rollinup.rollinup.component.utils.getPlatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_login

@Composable
fun LoginTopAppBar(
    onInfoClick: () -> Unit,
) {
    val platform = getPlatform()
    when (platform) {
        Platform.ANDROID, Platform.IOS -> {
            LoginTopBar(
                onInfoClick = onInfoClick
            )
        }

        else -> {}
    }
}

@Composable
fun LoginTopBar(
    onInfoClick: () -> Unit = {},
) {
    BaseTopBar(
        onNavigateUp = {},
        title = "",
        showNavigateUp = false,
        color = TopAppBarDefaults.topAppBarColors.copy(
            containerColor = theme.primary
        ),
        actionContent = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.label_login),
                    style = Style.header,
                    color = theme.textBtnPrimary
                )
                Box(
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(4.dp),
                            color = theme.success
                        )
                        .clickable() {
                            onInfoClick()
                        }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_info_line_24),
                        tint = theme.textBtnPrimary,
                        contentDescription = "Device Id",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(2.dp)
                    )
                }
            }
        }
    )
}