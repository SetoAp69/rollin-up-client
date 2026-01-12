package com.rollinup.rollinup.component.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.now
import com.rollinup.rollinup.component.model.Orientation
import com.rollinup.rollinup.component.model.Platform
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime


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

@Composable
fun getScreenHeight(): Dp {
    val currentWindowHeight = LocalWindowInfo.current.containerSize.height
    val density = LocalDensity.current

    return with(density) {
        currentWindowHeight.toDp()
    }
}

@Composable
fun getScreenSize(): Pair<Dp, Dp> = getScreenWidth() to getScreenHeight()

expect fun getPlatform(): Platform

@Composable
fun String.toAnnotatedString(): AnnotatedString {
    val regex = Regex("\\*\\*(.*?)\\*\\*")

    val matches = regex.findAll(this)

    val annotatedString = buildAnnotatedString {
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
    }.ifEmpty {
        AnnotatedString(this)
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
            composed() {
                this.modifier()
            }
        } else Modifier
    )
}

fun getCurrentDateAsList(): List<LocalDateTime> {
    val date = LocalDate.now()
    val from = LocalDateTime(date, LocalTime(0, 0, 0))
    val to = LocalDateTime(date, LocalTime(23, 59, 59))
    return listOf(from, to)
}

fun getFileName(dateRange: List<LocalDate>, fileName: String): String {
    return if (dateRange.isEmpty())
        fileName
    else{
        val date = if(dateRange.size==1) dateRange.first().toString() else "${dateRange.first()}-${dateRange.last()}"
        "${fileName}_$date"
    }
}

@Composable
expect fun getDeviceId(): String

@Composable
expect fun getOrientation(): Orientation

@Composable
expect fun getVersion():String