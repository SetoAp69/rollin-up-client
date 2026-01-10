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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
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
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_eye_close_line_24
import rollin_up.composeapp.generated.resources.ic_eye_open_line_24
import rollin_up.composeapp.generated.resources.ic_phone_line_24
import rollin_up.composeapp.generated.resources.ic_search_line_24
import rollin_up.composeapp.generated.resources.label_toggle_show_password
import rollin_up.composeapp.generated.resources.ph_search


/**
 * A specialized text field for entering phone numbers.
 *
 * Automatically filters input to allow only digits and limits the length.
 * Includes a phone icon and keyboard configuration.
 *
 * @param value The current text value.
 * @param onValueChange Callback for when the value changes. Input is sanitized.
 * @param placeholder Text to display when empty.
 * @param modifier Modifier for the layout.
 * @param title Label displayed above the field.
 * @param titleStyle Typography for the title.
 * @param isError If true, displays error styling.
 * @param errorMsg Error message text to show below the field.
 * @param isRequired If true, adds a visual indicator to the title.
 * @param isEnabled Controls enabled state.
 * @param isReadOnly Controls read-only state.
 * @param maxChar Maximum number of characters allowed.
 * @param keyboardActions Actions for keyboard events (e.g., Done).
 */
@Composable
fun PhoneNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "08xxxxxxxxxx",
    modifier: Modifier = Modifier,
    title: String = "",
    titleStyle: TextStyle = Style.body,
    isError: Boolean = false,
    errorMsg: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    maxChar: Int = 15, // Normal phone number limit
    keyboardActions: KeyboardActions? = null,
) {
    val iconColor = when {
        isError -> theme.textError
        !isEnabled -> theme.textFieldStrokeDisabled
        else -> theme.textFieldText
    }

    fun sanitize(input: String): String {
        return input.filter { it.isDigit() }.take(maxChar)
    }

    TextFieldTitle(
        title = title,
        isRequired = isRequired,
        textStyle = titleStyle,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            BaseTextField(
                value = value,
                onValueChange = { onValueChange(sanitize(it)) },
                placeholder = placeholder,
                modifier = modifier,
                isError = isError,
                isEnabled = isEnabled,
                isReadOnly = isReadOnly,
                maxChar = maxChar,
                maxLines = 1,
                minLines = 1,
                isSingleLine = true,
                leadingContent = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Res.drawable.ic_phone_line_24), // <-- change to your icon
                        contentDescription = null,
                        tint = iconColor
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = keyboardActions,
            )

            TextError(
                text = errorMsg ?: "",
                isError = isError
            )
        }
    }
}

/**
 * A wrapper component that displays a label title above content.
 *
 * @param title The text label.
 * @param textStyle Typography style for the label.
 * @param isRequired If true, appends a red asterisk to the label.
 * @param color Text color for the label.
 * @param modifier Modifier for the container.
 * @param content The composable content to be labeled (usually an input field).
 */
@Composable
fun TextFieldTitle(
    title: String,
    textStyle: TextStyle = Style.body,
    isRequired: Boolean = false,
    color: Color = theme.bodyText,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(itemGap4),
        horizontalAlignment = Alignment.Start
    ) {
        if (title.isNotBlank()) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = title,
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

/**
 * Displays an error message string with error styling.
 *
 * @param text The error message.
 * @param isError Controls visibility.
 * @param style Typography style for the message.
 */
@Composable
fun TextError(
    text: String,
    isError: Boolean,
    style: TextStyle = Style.body,
) {
    if (isError && text.isNotBlank()) {
        Text(
            text = text,
            style = style,
            color = theme.textError
        )
    }
}

/**
 * A text field for entering decimal numbers.
 *
 * Enforces numeric input and clamps the value between [minValue] and [maxValue].
 *
 * @param value The current double value.
 * @param onValueChange Callback with the new value.
 * @param maxValue Maximum allowed value.
 * @param minValue Minimum allowed value.
 * @param placeHolder Text to show when empty.
 * @param height Specific height for the field.
 * @param isEnabled Controls enabled state.
 * @param title Label above the field.
 * @param titleStyle Typography for the title.
 * @param isError Shows error styling if true.
 * @param errorMsg Error message text.
 * @param isReadOnly Controls read-only state.
 * @param modifier Modifier for the layout.
 * @param isRequired Show required indicator.
 * @param leadingContent Optional content at start of field.
 * @param trailingContent Optional content at end of field.
 * @param keyboardActions Actions for keyboard events.
 */
@Composable
fun DecimalNumberTextField(
    value: Double,
    onValueChange: (Double) -> Unit,
    maxValue: Double = Double.MAX_VALUE,
    minValue: Double = Double.MIN_VALUE,
    placeHolder: String,
    height: Dp? = null,
    isEnabled: Boolean = true,
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
        title = title,
        isRequired = isRequired,
        textStyle = titleStyle,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            BaseTextField(
                value = value.toString(),
                onValueChange = { value ->
                    val intValue = value.toDoubleOrNull() ?: 0.0
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

/**
 * A text field for entering integer numbers.
 *
 * Enforces numeric input and clamps the value between [minValue] and [maxValue].
 *
 * @param value The current integer value.
 * @param onValueChange Callback with the new value.
 * @param maxValue Maximum allowed value.
 * @param minValue Minimum allowed value.
 * @param placeHolder Text to show when empty.
 * @param height Specific height for the field.
 * @param isEnabled Controls enabled state.
 * @param title Label above the field.
 * @param titleStyle Typography for the title.
 * @param isError Shows error styling if true.
 * @param errorMsg Error message text.
 * @param isReadOnly Controls read-only state.
 * @param modifier Modifier for the layout.
 * @param isRequired Show required indicator.
 * @param leadingContent Optional content at start of field.
 * @param trailingContent Optional content at end of field.
 * @param keyboardActions Actions for keyboard events.
 */
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
        title = title,
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

/**
 * A dedicated text field for search functionality.
 *
 * Includes a search icon, handles IME Search actions, and syncs internal state.
 *
 * @param title Optional label above the field.
 * @param onValueChange Callback for every text change.
 * @param value The current search query.
 * @param placeholder Placeholder text.
 * @param onSearch Callback triggered when the search action is performed.
 */
@Composable
fun SearchTextField(
    title: String = "",
    onValueChange: (String) -> Unit,
    value: String,
    placeholder: String = stringResource(Res.string.ph_search),
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

/**
 * A text field for password entry.
 *
 * Features:
 * - Obscured text by default.
 * - Toggle button to show/hide password.
 * - Auto-hides password after a delay.
 * - Restricted max character length.
 *
 * @param value Current password text.
 * @param onValueChange Callback for text changes.
 * @param placeholder Text to show when empty.
 * @param modifier Modifier for the layout.
 * @param title Label above the field.
 * @param titleStyle Typography for the title.
 * @param isError Shows error styling if true.
 * @param errorMsg Error message text.
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    title: String = "",
    layoutId:String = "input",
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
        delay(5000)
        if (showPassword) showPassword = false
    }

    TextFieldTitle(
        title = title,
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
                layoutId = layoutId,
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
                            contentDescription = stringResource(Res.string.label_toggle_show_password)
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

/**
 * A general-purpose text field wrapper with support for titles, errors, and icons.
 *
 * @param value Current text value.
 * @param onValueChange Callback for text changes.
 * @param placeholder Text to show when empty.
 * @param isRequired If true, marks title with asterisk.
 * @param maxChar Maximum characters allowed.
 * @param title Label above the field.
 * @param maxLines Maximum visual lines.
 * @param height Specific height.
 * @param isEnabled Controls interaction.
 * @param titleStyle Typography for the title.
 * @param modifier Layout modifier.
 * @param isError Shows error styling.
 * @param errorMsg Error message text.
 * @param isSingleLine Restricts input to one line.
 * @param isReadOnly Prevents editing.
 * @param leadingIcon Icon resource at start.
 * @param trailingIcon Icon resource at end.
 * @param keyboardActions Actions for keyboard events.
 * @param keyboardOptions Options for keyboard type/behavior.
 * @param visualTransformation Visual transformation for input (e.g. password).
 */
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
    layoutId:String = "input",
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
        title = title,
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
                layoutId = layoutId,
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

/**
 * The core implementation of the text field using [BasicTextField].
 *
 * Handles styling, coloring based on state (error/disabled), and structure (decoration box).
 *
 * @param value Current text.
 * @param onValueChange Callback for changes.
 * @param placeholder Placeholder text.
 * @param modifier Layout modifier.
 * @param height Specific height.
 * @param isError Error state.
 * @param isEnabled Enabled state.
 * @param isReadOnly Read-only state.
 * @param maxLines Max visible lines.
 * @param minLines Min visible lines.
 * @param maxChar Max characters allowed (truncates input).
 * @param leadingContent Composable at start.
 * @param trailingContent Composable at end.
 * @param isSingleLine Single line restriction.
 * @param keyboardActions Keyboard actions.
 * @param keyboardOptions Keyboard options.
 * @param visualTransformation Visual transformation.
 */
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
    layoutId:String = "input",
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
            .testTag(layoutId)
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


/**
 * Default configuration values for text fields.
 */
object TextFieldDefaults {
    val shape
        @Composable get() =
            if (isCompact) RoundedCornerShape(12.dp) else RoundedCornerShape(12.dp)
    val height
        @Composable get() = if (isCompact) 50.dp else 50.dp
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