package com.rollinup.rollinup.component.navigationrail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_hamburger_line_24

@Composable
fun NavigationRail(
    menu: List<NavigationMenu>,
    isExpandable: Boolean,
    state: NavigationRailState = rememberNavigationRailState(
        menu.firstOrNull() ?: MainRoute.DashBoardRoute
    ),
    onClickMenu: (NavigationMenu) -> Unit,
    itemGap: Dp = itemGap8,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
) {
    val animateState = animateDpAsState(state.getMaxWidth())

    val horizontalAlignment = if (state.isExpanded) {
        Alignment.Start
    } else {
        Alignment.CenterHorizontally
    }

    NavigationRail(
        modifier = Modifier
            .widthIn(max = animateState.value)
            .fillMaxHeight(),
        containerColor = theme.popUpBg,
        contentColor = theme.secondary50,
        header = if (isExpandable) {
            {
                NavigationHeader(
                    isExpanded = state.isExpanded,
                    onToggleExpanded = {
                        state.isExpanded = it
                    }
                )
            }
        } else {
            null
        }
    ) {
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = itemGap8)
        ) {
            menu.fastForEach {
                NavigationMenu(
                    menu = it,
                    isSelected = state.currentMenu == it,
                    onClickMenu = {
                        state.onClickMenu(it)
                        onClickMenu(it)
                    },
                    isExpanded = state.isExpanded
                )
                Spacer(itemGap)
            }
        }
    }
}

@Composable
private fun NavigationHeader(
    isExpanded: Boolean,
    onToggleExpanded: (Boolean) -> Unit,
) {
    val icon =
        if (isExpanded)
            Res.drawable.ic_close_line_24
        else
            Res.drawable.ic_hamburger_line_24

    val alignment = if (isExpanded) Alignment.CenterStart else Alignment.Center
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = itemGap8),
        contentAlignment = alignment
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Expand",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(46.dp)
                .clickable {
                    onToggleExpanded(!isExpanded)
                }
                .padding(10.dp),
            tint = theme.secondary50
        )
    }
}

@Composable
fun NavigationMenu(
    menu: NavigationMenu,
    isSelected: Boolean,
    onClickMenu: (NavigationMenu) -> Unit,
    isExpanded: Boolean,
) {
    val background = if (isSelected) {
        Modifier.background(
            color = theme.primary
        )
    } else {
        Modifier
    }

    val alignment = if (isExpanded) Alignment.CenterStart else Alignment.Center

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable {
                onClickMenu(menu)
            }
            .size(46.dp)
            .then(background)
            .padding(10.dp),
        contentAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(menu.icon),
                contentDescription = menu.title,
                tint = if (isSelected) theme.textBtnPrimary else theme.secondary50
            )

            if (isExpanded) {
                Spacer(itemGap4)
                Text(
                    text = menu.title,
                    style = Style.label,
                    color = if (isSelected) theme.textBtnPrimary else theme.secondary50
                )
            }
        }
    }
}

class NavigationRailState(
    val initialMenu: NavigationMenu,
    initialExpandedState: Boolean = false,
) {
    var isExpanded by mutableStateOf(initialExpandedState)
    var currentMenu by mutableStateOf(initialMenu)
    fun onClickMenu(menu: NavigationMenu) {
        currentMenu = menu
    }

    fun getMaxWidth(): Dp {
        return if (isExpanded) 300.dp else 72.dp
    }
}

@Composable
fun rememberNavigationRailState(
    initialMenu: NavigationMenu,
    initialExpandedState: Boolean = false,
) =
    remember {
        NavigationRailState(
            initialMenu = initialMenu,
            initialExpandedState = initialExpandedState
        )
    }

