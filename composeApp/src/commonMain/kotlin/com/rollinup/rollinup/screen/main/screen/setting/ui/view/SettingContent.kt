package com.rollinup.rollinup.screen.main.screen.setting.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.UiMode
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.topbar.TopBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_exit_line_24
import rollin_up.composeapp.generated.resources.label_auto
import rollin_up.composeapp.generated.resources.label_dark_mode
import rollin_up.composeapp.generated.resources.label_light_mode

@Composable
fun SettingContent(
    uiMode: UiMode,
    onLogout: () -> Unit,
    onUiModeChange: (UiMode) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Setting",
                showNavigateUp = false,
                onClickMenu = {},
                menu = emptyList(),
                onNavigateUp = {},
            )
        },
        showBottomBar = true
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4),
            modifier = Modifier.padding(screenPadding)
        ) {
            UiModeSetting(
                value = uiMode,
                onValueChanges = onUiModeChange
            )
            LogOut {
                onLogout()
            }
        }
    }
}


@Composable
private fun LogOut(
    onLogout: () -> Unit,
) {
    Card(onClick = onLogout) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Logout",
                style = Style.popupBody,
                color = theme.bodyText
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(Res.drawable.ic_exit_line_24),
                contentDescription = null,
                tint = theme.textPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun UiModeSetting(
    value: UiMode,
    onValueChanges: (UiMode) -> Unit,
) {
    val title = when (value) {
        UiMode.DARK -> stringResource(Res.string.label_dark_mode)
        UiMode.AUTO -> stringResource(Res.string.label_auto)
        UiMode.LIGHT -> stringResource(Res.string.label_light_mode)
    }

    Card {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Style.popupBody,
                color = theme.bodyText
            )
            Spacer(Modifier.weight(1f))
            UiModeSwitch(
                value = value,
                onValueChanges = onValueChanges
            )
        }
    }
}
