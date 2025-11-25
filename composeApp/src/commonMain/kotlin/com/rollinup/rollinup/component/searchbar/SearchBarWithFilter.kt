package com.rollinup.rollinup.component.searchbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults.DecorationBox
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_search_line_24

@Composable
fun SearchBarWithMenu(
    searchQuery: String,
    onSearch: (String) -> Unit,
    menu: List<Menu> = emptyList(),
    onClickMenu: (Menu) -> Unit = {},
) {
    var tempSearchQuery by remember { mutableStateOf(searchQuery) }
    LaunchedEffect(searchQuery) {
        if (searchQuery != tempSearchQuery) tempSearchQuery = searchQuery
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(itemGap4),
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            SearchField(
                searchQuery = tempSearchQuery,
                onUpdateValue = { tempSearchQuery = it },
                onSearch = {
                    tempSearchQuery = it
                    onSearch(tempSearchQuery)
                }
            )
        }
        menu.take(3).forEach {
            SearchBarMenu(
                menu = it,
                onClickMenu = onClickMenu
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchField(
    searchQuery: String,
    onUpdateValue: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
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
        },
        onSearch = {
            onSearch(searchQuery)
        },
    )
    val keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search,
        hintLocales = LocaleList.current
    )
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = searchQuery,
        onValueChange = onUpdateValue,
        modifier = Modifier
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
                                onSearch("")
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
private fun SearchBarMenu(
    menu: Menu,
    onClickMenu: (Menu) -> Unit,
) {
    Icon(
        painter = painterResource(menu.icon),
        contentDescription = menu.title,
        tint = theme.textPrimary,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onClickMenu(menu)
            }
            .size(32.dp)
    )
}