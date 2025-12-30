@file:OptIn(ExperimentalMaterial3Api::class)

package com.rollinup.rollinup.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults.DecorationBox
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_arrow_left_line_24
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_search_line_24
import rollin_up.composeapp.generated.resources.label_search

/**
 * A standard top app bar component.
 *
 * Displays a navigation icon (back button), a title, and a list of optional action menus.
 *
 * @param onNavigateUp Callback triggered when the navigation icon (back arrow) is clicked.
 * @param title The title text to display in the center of the app bar.
 * @param menu A list of [Menu] items to display as action icons on the right side.
 * @param onClickMenu Callback triggered when a menu item is clicked.
 * @param scrollBehavior Scroll behavior to be attached to the TopAppBar (e.g., for collapsing).
 */
@Composable
fun TopBar(
    onNavigateUp: () -> Unit,
    title: String,
    menu: List<Menu> = emptyList(),
    onClickMenu: (Menu) -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val colors = TopAppBarColors(
        containerColor = theme.popUpBg,
        scrolledContainerColor = theme.popUpBg,
        navigationIconContentColor = theme.textPrimary,
        titleContentColor = theme.textPrimary,
        actionIconContentColor = theme.textPrimary
    )

    val actions: @Composable RowScope.() -> Unit = {
        Row(
            horizontalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            menu.forEach {
                TopBarMenu(
                    menu = it,
                    onClickMenu = onClickMenu
                )
            }
        }
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                style = Style.popupTitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_left_line_24),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        onNavigateUp()
                    }
                    .padding(4.dp)
                    .size(24.dp)
            )
        },
        windowInsets = WindowInsets(top = 0.dp),
        colors = colors,
        scrollBehavior = scrollBehavior,
        actions = actions
    )
}

/**
 * A specialized top app bar that includes an integrated search field.
 *
 * Features:
 * - Back navigation button.
 * - Search text input with "Search" IME action.
 * - Clear button (X) inside the search field.
 *
 * @param onNavigateUp Callback triggered when the back button is clicked.
 * @param searchQuery The current text in the search field.
 * @param onSearch Callback triggered when the search action is performed or text is cleared.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTopBar(
    onNavigateUp: () -> Unit,
    searchQuery: String,
    onSearch: (String) -> Unit,
) {
    var tempSearchQuery by remember { mutableStateOf(searchQuery) }
    LaunchedEffect(searchQuery) {
        if (searchQuery != tempSearchQuery) tempSearchQuery = searchQuery
    }

    val trailingIcon =
        if (tempSearchQuery.isEmpty()) {
            Res.drawable.ic_search_line_24
        } else {
            Res.drawable.ic_close_line_24
        }

    val shape = RoundedCornerShape(50)
    val keyboardActions = KeyboardActions(
        onDone = {
            onSearch(tempSearchQuery)
        },
        onSearch = {
            onSearch(tempSearchQuery)
        },
    )
    val keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search,
        hintLocales = LocaleList.current
    )
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = tempSearchQuery,
        onValueChange = { tempSearchQuery = it },
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(42.dp)
            .fillMaxWidth(),
        enabled = true,
        textStyle = Style.body.copy(color = theme.textFieldText),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        cursorBrush = SolidColor(value = theme.textFieldText),
        decorationBox = { innerTextField ->
            DecorationBox(
                value = tempSearchQuery,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    if (tempSearchQuery.isBlank()) {
                        Text(
                            text = stringResource(Res.string.label_search),
                            color = theme.textFieldPlaceHolder,
                            style = Style.body
                        )
                    }
                },
                contentPadding = PaddingValues(vertical = itemGap4, horizontal = itemGap8),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_left_line_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                onNavigateUp()
                            }
                            .padding(4.dp)
                            .size(24.dp),
                        tint = theme.textPrimary
                    )
                },
                container = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = theme.textFieldBackGround,
                                shape = shape
                            )
                    ) {

                    }
                },
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(searchQuery.isNotBlank()) {
                                onNavigateUp()
                                onSearch("")
                                focusManager.clearFocus()
                            }
                    ) {
                        Icon(
                            painter = painterResource(trailingIcon),
                            tint = theme.textFieldText,
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.colors
            )
        },
    )

}


/**
 * Internal helper to render a single menu action icon in the [TopBar].
 *
 * @param menu The menu item data.
 * @param onClickMenu Callback triggered when the icon is clicked.
 */
@Composable
internal fun TopBarMenu(
    menu: Menu,
    onClickMenu: (Menu) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable() {
                onClickMenu(menu)
            }
    ) {
        Icon(
            painter = painterResource(menu.icon),
            contentDescription = menu.title,
            tint = theme.textPrimary
        )
    }
}