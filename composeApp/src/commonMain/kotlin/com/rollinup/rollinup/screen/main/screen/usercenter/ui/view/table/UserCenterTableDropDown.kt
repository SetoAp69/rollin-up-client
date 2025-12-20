package com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastForEach
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterAction
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_no_action_available

@Composable
fun UserCenterTableDropDown(
    isShowDropdown: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    item: List<UserEntity>,
    onClickAction: (UserCenterAction) -> Unit,
) {
    val listAction = UserCenterAction.entries.filter { it.show(item) }

    DropDownMenu(
        isShowDropDown = isShowDropdown,
        onDismissRequest = onDismissRequest,
    ) {
        if (listAction.isEmpty()) {
            DropDownMenuItem(
                label = stringResource(Res.string.msg_no_action_available),
                onClick = { onDismissRequest(false) },
            )
        } else {
            listAction.fastForEach { action ->
                val color = getContentColor(action.severity)
                DropDownMenuItem(
                    label = action.label,
                    icon = action.icon,
                    iconTint = color.first,
                    textColor = color.second,
                ) {
                    onClickAction(action)
                    onDismissRequest(false)
                }
            }
        }
    }
}

@Composable
private fun getContentColor(severity: Severity): Pair<Color, Color> {
    return when (severity) {
        Severity.DANGER -> theme.danger to theme.danger
        else -> theme.textPrimary to theme.bodyText
    }

}