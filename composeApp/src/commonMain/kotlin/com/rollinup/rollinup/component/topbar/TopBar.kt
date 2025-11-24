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
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_search_line_24

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBar(
    onClickMenu: (Menu) -> Unit,
    menu: List<Menu>,
    onSearch: (String) -> Unit,
    onNavigateUp: () -> Unit,
    title: String = "",
    modifier: Modifier = Modifier,
    color: TopAppBarColors? = null,
    showNavigateUp: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val title = if (showSearch) "" else title
    val menu = menu.distinct().filterNot { it == Menu.SEARCH }
    val navigateUp: () -> Unit = {
        if (showSearch) {
            searchQuery = ""
            onSearch("")
            showSearch = false
        } else {
            onNavigateUp()
        }
    }

    BackHandler {
        navigateUp()
    }

    BaseTopBar(
        title = title,
        actionContent = {
            if (showSearch) {
                Box(modifier = Modifier.weight(1f)) {
                    SearchField(
                        onNavigateUp = {
                            showSearch = false
                            searchQuery = ""
                            onSearch("")
                        },
                        searchQuery = searchQuery,
                        onUpdateValue = { searchQuery = it },
                        onSearch = onSearch
                    )
                }
            } else {
                TopBarMenu(
                    menu = Menu.SEARCH,
                    onClickMenu = {
                        showSearch = true
                        onSearch("$showSearch")
                    }
                )
            }
            menu.forEach {
                TopBarMenu(
                    menu = it,
                    onClickMenu = onClickMenu
                )
            }
        },
        modifier = modifier,
        showNavigateUp = showNavigateUp,
        onNavigateUp = navigateUp,
        color = color,
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun TopBar(
    onClickMenu: (Menu) -> Unit,
    menu: List<Menu>,
    showNavigateUp: Boolean = true,
    onNavigateUp: () -> Unit,
    title: String = "",
    modifier: Modifier = Modifier,
    color: TopAppBarColors? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val menu = menu.distinct().filterNot { it == Menu.SEARCH }
    BaseTopBar(
        title = title,
        actionContent = {
            menu.forEach {
                TopBarMenu(
                    menu = it,
                    onClickMenu = onClickMenu
                )
            }
        },
        modifier = modifier,
        showNavigateUp = showNavigateUp,
        onNavigateUp = onNavigateUp,
        color = color,
        scrollBehavior = scrollBehavior
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    title: String = "",
    actionContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    showNavigateUp: Boolean = true,
    onNavigateUp: () -> Unit = {},
    color: TopAppBarColors? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val color = color ?: TopAppBarDefaults.topAppBarColors
    val title = if (title.length > 25) title.take(8) + "..." + title.takeLast(8) else title

    TopAppBar(
        title = {},
        modifier = modifier,
        navigationIcon = {},
        actions = {
            TopBarContent(
                title = title,
                content = actionContent,
                showNavigateUp = showNavigateUp,
                navigateUp = onNavigateUp,
            )
        },
        colors = color,
        scrollBehavior = scrollBehavior
    )
}

object TopAppBarDefaults {
    val topAppBarColors: TopAppBarColors
        @Composable get() = TopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = theme.textPrimary,
            titleContentColor = theme.textPrimary,
            actionIconContentColor = theme.textPrimary
        )

}

@Composable
internal fun TopBarContent(
    title: String,
    content: @Composable RowScope.() -> Unit,
    showNavigateUp: Boolean,
    navigateUp: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = itemGap8+itemGap4
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showNavigateUp) {
            TopBarMenu(menu = Menu.BACK, onClickMenu = { navigateUp() })
            Spacer(itemGap8)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Start,
                    style = Style.header,
                    color = theme.textPrimary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(itemGap8, Alignment.End),
                modifier = Modifier
                    .weight(1f)

            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun SearchField(
    onNavigateUp: () -> Unit,
    searchQuery: String,
    onUpdateValue: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    BackHandler {
        onNavigateUp()
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val trailingIcon =
        if (searchQuery.isEmpty()) {
            Res.drawable.ic_search_line_24
        } else {
            Res.drawable.ic_close_line_24
        }

    val shape = RoundedCornerShape(8.dp)
    val keyboardActions = KeyboardActions(
        onDone = {
            onSearch(searchQuery)
            focusManager.clearFocus()
        },
        onSearch = {
            onSearch(searchQuery)
            focusManager.clearFocus()
        },
    )
    val keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search,
        hintLocales = LocaleList.current
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        focusRequester.captureFocus()
    }

    BasicTextField(
        value = searchQuery,
        onValueChange = onUpdateValue,
        modifier = Modifier
            .focusRequester(focusRequester)
            .height(32.dp)
            .fillMaxWidth(),
        enabled = true,
        textStyle = Style.body.copy(color = theme.textFieldText),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        cursorBrush = SolidColor(value = theme.textFieldText),
        decorationBox = { innerTextField ->
            DecorationBox(
                value = searchQuery,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    if (searchQuery.isBlank()) {
                        Text(
                            text = "Search",
                            color = theme.textFieldPlaceHolder,
                            style = Style.body
                        )
                    }
                },
                contentPadding = PaddingValues(vertical = itemGap4, horizontal = itemGap8),
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