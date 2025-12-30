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

/**
 * A custom Bottom Navigation Bar component that features a distinctive, floating center button.
 *
 * This component handles:
 * - Animated visibility (slide in/out).
 * - Automatic layout distribution with a "hole" reserved for the center button.
 * - Selection state management.
 * - Double-tap to refresh logic.
 *
 * @param showBottomBar Controls the visibility of the bottom bar with a slide animation.
 * @param listMenu The list of navigation items to display. Note: This list is truncated to 5 items.
 * @param onNavigate Callback triggered when a menu item is clicked and it is *not* the currently selected item.
 * @param state The state holder for the bottom bar, managing the currently selected menu.
 * @param onGetHeight Callback that reports the calculated height of the bottom bar (useful for padding scaffold content).
 * @param onRefresh Callback triggered when the *currently selected* menu item is clicked again.
 */
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


/**
 * Renders the prominent, circular center menu item.
 *
 * This component visually "breaks" the top edge of the bottom bar.
 *
 * @param menu The navigation data for this item.
 * @param isSelected Whether this item is currently active.
 * @param onMenuClick Callback for click events.
 */
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

/**
 * Renders a standard menu item located on the left or right of the center button.
 *
 * Features a small indicator line at the top when selected.
 *
 * @param menu The navigation data for this item.
 * @param isSelected Whether this item is currently active.
 * @param onMenuClick Callback for click events.
 */
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

/**
 * State holder for the [BottomBar].
 *
 * Manages the currently selected menu item.
 *
 * @param initialMenu The menu item selected by default.
 */
class BottomBarState(private val initialMenu: NavigationMenu) {
    var currentMenu: NavigationMenu by mutableStateOf(initialMenu)
        private set

    /** Updates the selected menu item. */
    fun onNavigate(menu: NavigationMenu) {
        currentMenu = menu
    }

    /** Resets the selection to the initial menu item. */
    fun reset() {
        currentMenu = initialMenu
    }
}

/**
 * Creates and remembers a [BottomBarState].
 *
 * @param initialMenu The menu item to be selected initially.
 */
@Composable
fun rememberBottomBarState(initialMenu: NavigationMenu) = remember {
    BottomBarState(initialMenu)
}
