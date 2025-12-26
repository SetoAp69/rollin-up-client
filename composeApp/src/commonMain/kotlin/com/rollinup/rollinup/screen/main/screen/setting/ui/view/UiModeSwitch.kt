package com.rollinup.rollinup.screen.main.screen.setting.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.rollinup.common.model.UiMode
import com.rollinup.rollinup.component.theme.theme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_dark_mode_line_24
import rollin_up.composeapp.generated.resources.ic_half_circle_line_24
import rollin_up.composeapp.generated.resources.ic_light_mode_line_24
import kotlin.math.roundToInt

@Composable
fun UiModeSwitch(
    value: UiMode,
    onValueChanges: (UiMode) -> Unit,
) {
    val density = LocalDensity.current
    val width = 100.dp
    val iconSize = 28.dp
    val scope = rememberCoroutineScope()
    val draggableAnchors = remember {
        DraggableAnchors<UiMode> {
            with(density) {
                UiMode.DARK at 0F
                UiMode.AUTO at ((width - iconSize - 8.dp) / 2).toPx()
                UiMode.LIGHT at (width - iconSize - 8.dp).toPx()
            }
        }
    }
    val anchoredDraggableState = remember {
        AnchoredDraggableState<UiMode>(
            initialValue = value,
            anchors = draggableAnchors,
        )
    }

    LaunchedEffect(value) {
        if (anchoredDraggableState.settledValue != value) {
            scope.launch {
                anchoredDraggableState.anchoredDrag(value) { _, _ ->
                    anchoredDraggableState
                }
            }
        }
    }

    LaunchedEffect(anchoredDraggableState.settledValue) {
        onValueChanges(anchoredDraggableState.settledValue)
    }

    Box(
        modifier = Modifier
            .border(width = 2.dp, color = theme.primary, shape = RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .width(width)
            .background(shape = RoundedCornerShape(50), color = theme.popUpBg)
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UiMode.entries.fastForEach { uiMode ->
                SwitchIcon(
                    size = iconSize,
                    uiMode = uiMode,
                    onClick = {
                        scope.launch {
                            anchoredDraggableState.animateTo(
                                targetValue = it,
                            )
                        }
                    },
                )
            }
        }
        SwitchDraggable(
            state = anchoredDraggableState,
            size = iconSize
        )
    }
}

@Composable
private fun SwitchIcon(
    size: Dp,
    uiMode: UiMode,
    onClick: (UiMode) -> Unit,
) {
    val icon = when (uiMode) {
        UiMode.DARK -> Res.drawable.ic_dark_mode_line_24
        UiMode.LIGHT -> Res.drawable.ic_light_mode_line_24
        UiMode.AUTO -> Res.drawable.ic_half_circle_line_24
    }
    Icon(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick(uiMode)
            }
            .size(size)
            .padding(4.dp),
        painter = painterResource(icon),
        tint = theme.textFieldText,
        contentDescription = null
    )
}

@Composable
private fun SwitchDraggable(
    state: AnchoredDraggableState<UiMode>,
    size: Dp = 28.dp,
) {
    val icon = when (state.currentValue) {
        UiMode.DARK -> Res.drawable.ic_dark_mode_line_24
        UiMode.LIGHT -> Res.drawable.ic_light_mode_line_24
        UiMode.AUTO -> Res.drawable.ic_half_circle_line_24
    }
    Box(
        modifier = Modifier
            .offset { IntOffset(x = state.offset.roundToInt(), y = 0) }
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal,
            )
            .clip(CircleShape)
            .background(theme.primary)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .size(20.dp),
            painter = painterResource(icon),
            tint = theme.textBtnPrimary,
            contentDescription = null
        )
    }
}


