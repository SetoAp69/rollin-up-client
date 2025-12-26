package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.theme

@Composable
fun LoginBackgroundShape(modifier: Modifier = Modifier) {
    val color = theme.primary
    val path = remember { Path() }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        val color = color

        path.moveTo(
            0f,
            size.height
        )



        path.cubicTo(
            size.width * 0.25f,
            size.height * 0.625f,
            size.width * 0.375f,
            size.height * 0.625f,
            size.width * 0.5f,
            size.height * 0.75f
        )

        path.cubicTo(
            size.width * 0.625f,
            size.height */*0.875f*/0.875f,
            size.width * 0.75f,
            size.height,
            size.width,
            size.height * 0.5f
        )

        path.lineTo(size.width, size.height * 0.5f)
        path.lineTo(size.width, 0f)
        path.lineTo(0f, 0f)
        path.close()

        drawPath(
            color = color,
            path = path
        )

    }

}