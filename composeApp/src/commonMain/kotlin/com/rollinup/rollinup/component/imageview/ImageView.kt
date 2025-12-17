package com.rollinup.rollinup.component.imageview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.theme
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_image_broken_fill_24

@Composable
fun Image(
    painter: Painter,
    width: Dp = 48.dp,
    height: Dp = 48.dp,
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(width, height)
            .clip(RoundedCornerShape(itemGap4)),
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun Image(
    url: String,
    height: Dp = 48.dp,
    width: Dp = 48.dp,
    onClick: ((String) -> Unit)? = null,
) {
    val painter = rememberAsyncImagePainter(url)
    val state = painter.state.collectAsStateWithLifecycle().value
    val isLoadSuccess = state is AsyncImagePainter.State.Success
    var showImageView by remember { mutableStateOf(false) }

    AsyncImage(
        modifier = Modifier
            .size(width, height),
        painter = painter,
        state = state,
        onClickImage = {
            if (isLoadSuccess) {
                onClick?.invoke(url) ?: { showImageView = true }
            }
        }
    )

    ImageView(
        painter = painter,
        state = state,
        onDismissRequests = { showImageView = it },
        showView = showImageView
    )

}


@Composable
fun ImageView(
    showView: Boolean,
    onDismissRequests: (Boolean) -> Unit,
    url: String,
) {
    if (showView) {
        val dataStore = koinInject<LocalDataStore>()
        val token = runBlocking { dataStore.getToken() }
        val model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .httpHeaders(
                headers = NetworkHeaders
                    .Builder()
                    .add("Authorization", "Bearer $token")
                    .build()
            )
            .build()
        val painter = rememberAsyncImagePainter(model)

        val state = painter.state.collectAsStateWithLifecycle().value
        var zoomScale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Dialog(
            onDismissRequest = { onDismissRequests(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
                    zoomScale = (zoomScale * zoomChange).coerceIn(1f, 5f)

                    val extraWidth = (zoomScale - 1) * constraints.maxWidth
                    val extraHeight = (zoomScale - 1) * constraints.maxHeight

                    val maxX = extraWidth / 2
                    val maxY = extraHeight

                    offset = Offset(
                        x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                        y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
                    )
                }

                AsyncImage(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = zoomScale,
                                scaleY = zoomScale,
                                translationY = offset.y,
                                translationX = offset.x
                            )
                            .transformable(transformableState),
                    state = state,
                    painter = painter,
                )
            }
        }

    }
}

@Composable
fun ImageView(
    showView: Boolean,
    onDismissRequests: (Boolean) -> Unit,
    painter: AsyncImagePainter,
    state: AsyncImagePainter.State,
) {
    if (showView) {
        var zoomScale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Dialog(
            onDismissRequest = { onDismissRequests(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
                    zoomScale = (zoomScale * zoomChange).coerceIn(1f, 5f)

                    val extraWidth = (zoomScale - 1) * constraints.maxWidth
                    val extraHeight = (zoomScale - 1) * constraints.maxHeight

                    val maxX = extraWidth / 2
                    val maxY = extraHeight

                    offset = Offset(
                        x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                        y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
                    )
                }

                AsyncImage(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = zoomScale,
                                scaleY = zoomScale,
                                translationY = offset.y,
                                translationX = offset.x
                            )
                            .transformable(transformableState),
                    state = state,
                    painter = painter,
                )
            }
        }
    }
}

@Composable
private fun AsyncImage(
    modifier: Modifier = Modifier,
    state: AsyncImagePainter.State,
    painter: Painter,
    onClickImage: (() -> Unit)? = null,
) {
    when (state) {
        is AsyncImagePainter.State.Error, AsyncImagePainter.State.Empty -> {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(itemGap4))
                    .background(theme.textFieldBackGround)
                    .padding(itemGap4)
                    .then(modifier),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_image_broken_fill_24),
                    contentDescription = null,
                    tint = theme.textFieldText,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        is AsyncImagePainter.State.Loading -> {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(itemGap4))
                    .background(theme.textFieldBackGround)
                    .then(modifier),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = theme.primary,
                )
            }
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(itemGap4))
                    .clickable(onClickImage != null) {
                        onClickImage?.invoke()
                    }
                    .then(modifier),
                contentScale = ContentScale.Fit,
            )
        }
    }
}