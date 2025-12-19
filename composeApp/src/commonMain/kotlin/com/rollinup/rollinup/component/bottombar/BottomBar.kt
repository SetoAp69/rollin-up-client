package com.rollinup.rollinup.component.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.rollinup.rollinup.component.navigationrail.NavigationMenu
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    showBottomBar: Boolean,
    listMenu: List<NavigationMenu>,
    onNavigate: (NavigationMenu) -> Unit,
    state: BottomBarState = rememberBottomBarState(
        listMenu.firstOrNull() ?: MainRoute.DashBoardRoute
    ),
    onGetHeight: (Dp) -> Unit = { _ -> },
    onRefresh: () -> Unit,
) {
    val listMenu = listMenu.take(5)
    val middleIndex = (listMenu.size / 2)
    val density = LocalDensity.current
    val shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)

    LaunchedEffect(showBottomBar) {
        state.reset()
    }

    AnimatedVisibility(
        visible = showBottomBar,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        ambientColor = theme.bodyText,
                        spotColor = theme.bodyText,
                        shape = shape
                    )
                    .fillMaxWidth()
                    .clip(shape)
                    .background(theme.popUpBg)
                    .padding(top = 12.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
                    .onSizeChanged { size ->
                        val heightDp = with(density) { size.height.toDp() }
                        onGetHeight(heightDp)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                listMenu.fastForEachIndexed { idx, menu ->
                    if (idx == middleIndex) {
                        Spacer(56.dp)
                    } else {
                        BottomBarMenu(
                            menu = menu,
                            isSelected = state.currentMenu == menu,
                            onMenuClick = {
                                if (state.currentMenu == menu)
                                    onRefresh()
                                else
                                    onNavigate(menu); state.onNavigate(menu)
                            }
                        )
                    }
                }
            }
            BottomBarMainMenu(
                menu = listMenu[middleIndex],
                isSelected = state.currentMenu == listMenu[middleIndex],
                onMenuClick = { menu ->
                    if (menu == state.currentMenu)
                        onRefresh()
                    else
                        onNavigate(menu); state.onNavigate(menu)
                },
            )
        }
    }
}


@Composable
private fun BottomBarMainMenu(
    menu: NavigationMenu,
    isSelected: Boolean,
    onMenuClick: (NavigationMenu) -> Unit,
) {
    val icon = if (isSelected) menu.filledIcon else menu.icon
    val contentColor = if (isSelected) theme.primary else theme.textFieldText

    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap8),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    shape = CircleShape,
                    spotColor = theme.shadow,
                    ambientColor = theme.shadow,
                    elevation = 4.dp
                )
                .clip(CircleShape)
                .border(width = 8.dp, color = theme.secondary, shape = CircleShape)
                .clickable {
                    onMenuClick(menu)
                }
                .background(color = theme.primary)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = theme.textBtnPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = menu.title,
            color = contentColor,
            style = Style.body
        )
    }
}

@Composable
fun BottomBarMenu(
    menu: NavigationMenu,
    isSelected: Boolean,
    onMenuClick: (NavigationMenu) -> Unit,
) {
    val icon = if (isSelected) menu.filledIcon else menu.icon
    val contentColor = if (isSelected) theme.primary else theme.textFieldText
    val interactionSource = remember { MutableInteractionSource() }
    CustomRipple(Color.Transparent) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onMenuClick(menu)
                },
            verticalArrangement = Arrangement.spacedBy(itemGap4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) theme.primary else Color.Transparent,
                            shape = RoundedCornerShape(50)
                        )
                        .size(22.dp, 2.dp)
                )
                Spacer(itemGap4)
                Icon(
                    painter = painterResource(icon),
                    tint = theme.primary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = menu.title,
                color = contentColor,
                style = Style.body
            )
        }
    }
}

class BottomBarState(private val initialMenu: NavigationMenu) {
    var currentMenu: NavigationMenu by mutableStateOf(initialMenu)
        private set

    fun onNavigate(menu: NavigationMenu) {
        currentMenu = menu
    }

    fun reset() {
        currentMenu = initialMenu
    }
}

@Composable
fun rememberBottomBarState(initialMenu: NavigationMenu) = remember {
    BottomBarState(initialMenu)
}

//class BottomBarShape(
//    menuHeight: Dp,
//
//): Shape{
//    override fun createOutline(
//        size: Size,
//        layoutDirection: LayoutDirection,
//        density: Density,
//    ): Outline {
//
//    }
//
//}

