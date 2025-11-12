package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun OTPTextField(
    value: String,
    length: Int = 5,
    title: String = "Enter OTP Code",
    onValueChange: (String) -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(itemGap4),
    modifier: Modifier = Modifier,
    textError: String? = null,
    isError: Boolean = false,
) {
    val parsedValue = value.take(length)
    val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = Style.title,
            color = theme.bodyText
        )
        Spacer(itemGap8)
        BasicTextField(
            value = parsedValue,
            onValueChange = { value ->
                if (value.isBlank() || value.any { !it.isDigit() }) {
                    onValueChange("")
                    return@BasicTextField
                }
                if (value.length <= length) onValueChange(value)
            },
            keyboardOptions = keyboardOptions,
            modifier = modifier,
            maxLines = 1,
            singleLine = true,
            decorationBox = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = horizontalArrangement
                ) {
                    for (i in 0..length - 1) {
                        OTPTexFieldBox(
                            value = parsedValue.getOrNull(i)?.toString(),
                            isError = isError
                        )
                    }
                }
            },
        )

        Spacer(itemGap4)
        TextError(
            text = textError ?: "",
            isError = isError
        )
    }
}

@Composable
private fun OTPTexFieldBox(
    value: String?,
    isError: Boolean,
) {
    val backgroundColor: Color
    val textColor: Color

    when {
        isError -> {
            backgroundColor = theme.textFieldBackgroundError
            textColor = theme.textError
        }

        value != null -> {
            backgroundColor = theme.success50
            textColor = theme.textSuccess
        }

        else -> {
            backgroundColor = theme.textFieldBackGround
            textColor = Color.Transparent
        }
    }

    Box(
        modifier = Modifier
            .width(32.dp)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = backgroundColor
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center

    ) {
        Text(
            text = value ?: "",
            style = Style.headerBold.copy(fontWeight = FontWeight.W400),
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}