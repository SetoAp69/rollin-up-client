package com.rollinup.rollinup.component.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.Platform


val isCompact
    @Composable get() = getScreenWidth() < 600.dp

@Composable
fun getScreenWidth(): Dp {
    val currentWindowWidth = LocalWindowInfo.current.containerSize.width
    val density = LocalDensity.current

    return with(density) {
        currentWindowWidth.toDp()
    }
}

expect fun getPlatform(): Platform


@Composable
fun String.toAnnotatedString(): AnnotatedString {
    val regex = Regex("\\*\\*(.*?)\\*\\*")

    val matches = regex.findAll(this)

    val annotatedString = buildAnnotatedString {

//        append(this@toAnnotatedString)

        var startIndex = 0

        matches.forEach {
            append(
                this@toAnnotatedString.substring(startIndex, it.range.start).replace("**", "")
            )
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W600
                )
            ) {
                append(
                    it.groupValues[1]
                )
            }
            startIndex = it.range.last + 1

        }
        if (startIndex > this@toAnnotatedString.length) {
            append(
                this@toAnnotatedString.substring(startIndex)
            )
        }
    }

    return annotatedString
}

@Composable
fun Modifier.applyIf(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier,
): Modifier {
    return this.then(
        if (condition) {
            modifier()
        } else Modifier
    )
}

/*
*
* 0,1,2,[3,4,5,6],7,8,9,10,[11,12],13,14
*
*
* */