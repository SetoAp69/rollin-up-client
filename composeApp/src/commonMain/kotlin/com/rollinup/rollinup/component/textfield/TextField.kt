package com.rollinup.rollinup.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.textFieldPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_eye_close_line_24
import rollin_up.composeapp.generated.resources.ic_eye_open_line_24
import rollin_up.composeapp.generated.resources.ic_search_line_24


@Composable
fun TextFieldTitle(
    text: String,
    textStyle: TextStyle = Style.body,
    isRequired: Boolean = false,
    color: Color = theme.bodyText,
    content: @Composable () -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap4),
        horizontalAlignment = Alignment.Start
    ) {
        if (text.isNotBlank()) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = text,
                    style = textStyle,
                    color = color
                )
                if (isRequired) {
                    Spacer(itemGap4)
                    Text(
                        text = "*",
                        style = textStyle,
                        color = theme.textError
                    )
                }
            }
        }
        content()
    }
}

@Composable
fun TextError(
    text: String,
    isError: Boolean,
    style: TextStyle = Style.body,
) {
    if (isError) {
        Text(
            text = text,
            style = style,
            color = theme.textError
        )
    }
}

@Composable
fun NumberTextField(
    value: Int,
    onValueChange: (Int) -> Unit,
    maxValue: Int = Int.MAX_VALUE,
    minValue: Int = Int.MIN_VALUE,
    placeHolder: String,
    height: Dp? = null,
    isEnabled: Boolean,
    title: String = "",
    titleStyle: TextStyle = Style.body,
    isError: Boolean = false,
    errorMsg: String? = null,
    isReadOnly: Boolean = false,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions? = null,
) {
    TextFieldTitle(
        text = title,
        isRequired = isRequired,
        textStyle = titleStyle,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            BaseTextField(
                value = value.toString(),
                onValueChange = { value ->
                    val intValue = value.toIntOrNull() ?: 0
                    val value =
                        if (intValue > maxValue) maxValue else if (intValue < minValue) minValue else intValue

                    onValueChange(value)
                },
                placeholder = placeHolder,
                modifier = modifier,
                height = height,
                isError = isError,
                isEnabled = isEnabled,
                isReadOnly = isReadOnly,
                maxLines = 1,
                minLines = 1,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                isSingleLine = true,
                keyboardActions = keyboardActions,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )
            TextError(
                text = errorMsg ?: "",
                isError = isError,
            )
        }
    }
}

@Composable
fun SearchTextField(
    title: String = "",
    onValueChange: (String) -> Unit,
    value: String,
    placeholder: String = "Enter keyword",
    onSearch: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf(value) }
    val keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search,
    )
    val keyboardAction = KeyboardActions(
        onSearch = { onSearch(searchQuery) },
        onDone = { onSearch(searchQuery) }
    )

    LaunchedEffect(value) {
        if (searchQuery != value) {
            searchQuery = value
        }
    }

    TextField(
        title = title,
        leadingIcon =
            if (searchQuery.isBlank()) {
                Res.drawable.ic_search_line_24
            } else {
                null
            },
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onValueChange(it)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardAction,
        placeholder = placeholder
    )

}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    title: String = "",
    titleStyle: TextStyle = Style.body,
    isError: Boolean = false,
    errorMsg: String? = null,
) {
    var showPassword by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val visualTransformation =
        if (showPassword) VisualTransformation.None else PasswordVisualTransformation('*')
    val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        autoCorrectEnabled = false,
    )
    val icon =
        if (showPassword)
            painterResource(Res.drawable.ic_eye_open_line_24)
        else
            painterResource(Res.drawable.ic_eye_close_line_24)

    val iconColor = if (isError) theme.textError else theme.textFieldText

    LaunchedEffect(showPassword) {
        delay(500)
        if (showPassword) showPassword = false
    }

    TextFieldTitle(
        text = title,
        textStyle = titleStyle,
        isRequired = false,
    ) {
        Column {
            BaseTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                modifier = modifier,
                isError = isError,
                isReadOnly = false,
                maxLines = 1,
                minLines = 1,
                maxChar = 30,
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                showPassword = !showPassword
                            }

                    ) {
                        Icon(
                            painter = icon,
                            tint = iconColor,
                            contentDescription = "toggle show password"
                        )
                    }
                },
                isSingleLine = true,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation
            )
            Spacer(itemGap4)
            TextError(
                text = errorMsg ?: "",
                isError = isError
            )
        }
    }


}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isRequired: Boolean = false,
    maxChar: Int = Int.MAX_VALUE,
    title: String = "",
    maxLines: Int = 1,
    height: Dp? = null,
    isEnabled: Boolean = true,
    titleStyle: TextStyle = Style.body,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMsg: String? = null,
    isSingleLine: Boolean = true,
    isReadOnly: Boolean = false,
    leadingIcon: DrawableResource? = null,
    trailingIcon: DrawableResource?? = null,
    keyboardActions: KeyboardActions? = null,
    keyboardOptions: KeyboardOptions? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val iconColor = when {
        isError -> theme.textError
        !isEnabled -> theme.textFieldStrokeDisabled
        else -> theme.textFieldText
    }

    val leadingIcon: (@Composable () -> Unit)? =
        leadingIcon?.let {
            {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }


    val trailingIcon: (@Composable () -> Unit)? =
        trailingIcon?.let {
            {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }


    TextFieldTitle(
        text = title,
        isRequired = isRequired,
        textStyle = titleStyle,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            BaseTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                modifier = modifier,
                height = height,
                isError = isError,
                isEnabled = isEnabled,
                isReadOnly = isReadOnly,
                maxLines = maxLines,
                minLines = 1,
                maxChar = maxChar,
                leadingContent = leadingIcon,
                trailingContent = trailingIcon,
                isSingleLine = isSingleLine,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation
            )
            TextError(
                text = errorMsg ?: "",
                isError = isError,
            )
        }
    }
}

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    height: Dp? = null,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    maxLines: Int = 1,
    minLines: Int = 0,
    maxChar: Int = Int.MAX_VALUE,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    isSingleLine: Boolean = true,
    keyboardActions: KeyboardActions? = null,
    keyboardOptions: KeyboardOptions? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardOptions = keyboardOptions ?: KeyboardOptions.Default
    val keyboardActions = keyboardActions ?: KeyboardActions.Default
    val textColor: Color
    val backGroundColor: Color
    val placeholderColor: Color

    val height = height
        ?: if (isSingleLine) TextFieldDefaults.height else TextFieldDefaults.height * 2

    when {
        isError -> {
            textColor = theme.textError
            backGroundColor = theme.textFieldBackgroundError
            placeholderColor = theme.textError
        }

        !isEnabled -> {
            textColor = theme.textFieldStrokeDisabled
            backGroundColor = theme.textFieldBgDisabled
            placeholderColor = theme.textFieldStrokeDisabled
        }

        else -> {
            textColor = theme.textFieldText
            backGroundColor = theme.textFieldBackGround
            placeholderColor = theme.textFieldPlaceHolder
        }
    }

    BasicTextField(
        value = value,
        onValueChange = { value ->
            val newValue = if (value.length > maxChar) value.take(maxChar) else value
            onValueChange(newValue)
        },
        modifier = modifier
            .height(height)
            .fillMaxWidth(),
        enabled = isEnabled,
        readOnly = isReadOnly,
        textStyle = Style.body.copy(color = textColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = isSingleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(value = theme.textFieldText),
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = isEnabled,
                singleLine = isSingleLine,
                interactionSource = interactionSource,
                isError = isError,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = placeholderColor,
                        style = Style.body
                    )
                },
                leadingIcon = leadingContent,
                trailingIcon = trailingContent,
                colors = TextFieldDefaults.colors,
                contentPadding = textFieldPadding,
                visualTransformation = visualTransformation,
                container = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(
                                shape = TextFieldDefaults.shape,
                                color = backGroundColor
                            )
                    ) {
                    }
                }
            )
        },
    )
}


object TextFieldDefaults {
    val shape
        @Composable get() =
            if (isCompact) RoundedCornerShape(12.dp) else RoundedCornerShape(12.dp)
    val height
        @Composable get() = if (isCompact) 50.dp else 50.dp //TODO : Changes if it looks sucks
    val colors
        @Composable get() = androidx.compose.material3.TextFieldDefaults.colors().copy(
            focusedTextColor = theme.textFieldText,
            unfocusedTextColor = theme.textFieldText,
            disabledTextColor = theme.textFieldStrokeDisabled,
            errorTextColor = theme.danger,
            focusedContainerColor = theme.textFieldBackGround,
            unfocusedContainerColor = theme.textFieldBackGround,
            disabledContainerColor = theme.textFieldBgDisabled,
            errorContainerColor = theme.textFieldBackgroundError,
            cursorColor = theme.textFieldText,
            errorCursorColor = theme.textError,
            unfocusedIndicatorColor = theme.textFieldStroke,
            disabledIndicatorColor = theme.textFieldStrokeDisabled,
            errorIndicatorColor = theme.textError,
        )
}