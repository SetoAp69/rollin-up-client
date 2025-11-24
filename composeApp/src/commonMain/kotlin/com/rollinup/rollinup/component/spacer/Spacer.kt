package com.rollinup.rollinup.component.spacer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.utils.isCompact

@Composable
fun RowScope.Spacer(width: Dp) {
    Spacer(modifier = Modifier.padding(start = width))
}

@Composable
fun ColumnScope.Spacer(height: Dp) {
    Spacer(modifier = Modifier.padding(top = height))
}

//Item Gap Size
val itemGap8
    @Composable get() = if (isCompact) 8.dp else 12.dp

val itemGap4
    @Composable get() = if (isCompact) 4.dp else 6.dp


//Padding Size
val textFieldPadding
    @Composable get() =
        if (isCompact)
            PaddingValues(vertical = 8.dp, horizontal = 20.dp)
        else
            PaddingValues(vertical = 8.dp, horizontal = 20.dp)

val screenPadding
    @Composable get() = if (isCompact) 12.dp else 16.dp

val screenPaddingValues
    @Composable get() =
        if (isCompact)
            PaddingValues(vertical = 12.dp, horizontal = 12.dp)
        else
            PaddingValues(vertical = 16.dp, horizontal = 16.dp)
