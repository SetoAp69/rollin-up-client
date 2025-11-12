package com.rollinup.rollinup.screen.test.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.apiservice.model.paging.PagingDummyEntity
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.test.ui.viewmodel.PagingDemoViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun PagingDummyDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    if (isShowDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest(false)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Scaffold(
                modifier = Modifier.background(color = theme.popUpBg)
            ) {
                PagingDummy(isShowDialog)
            }
        }
    }
}

@Composable
fun PagingDummy(
    isShowDialog: Boolean,
) {
    val viewModel: PagingDemoViewModel = koinViewModel()
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    DisposableEffect(isShowDialog) {
        if (isShowDialog) {
            viewModel.init()
        }
        onDispose { }
    }

    PagingColumn(
        pagingData = pagingData,
        itemContent = {
            PagingDemoItem(it)
        },
        loadingContent = {
            PagingDemoLoading()
        },
        onRefresh = { viewModel.refresh() },
    )
}


@Composable
fun PagingDemoItem(
    item: PagingDummyEntity,
) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            Text(
                text = item.id.toString(),
                color = theme.bodyText,
                style = Style.body
            )
            Text(
                text = item.title,
                color = theme.bodyText,
                style = Style.title
            )
            Text(
                text = item.price.toString(),
                color = theme.bodyText,
                style = Style.body
            )

        }
    }
}

@Composable
fun PagingDemoLoading() {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            ShimmerEffect(60.dp)
            ShimmerEffect(120.dp)
            ShimmerEffect(150.dp)
        }
    }
}