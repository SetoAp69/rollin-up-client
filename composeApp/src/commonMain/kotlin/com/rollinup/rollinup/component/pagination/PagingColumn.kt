package com.rollinup.rollinup.component.pagination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.rollinup.rollinup.component.pullrefresh.PullRefresh
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.theme

@Composable
fun <T : Any> PagingColumn(
    pagingData: LazyPagingItems<T>,
    itemContent: @Composable (T) -> Unit,
    loadingContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentArrangement: Arrangement.Vertical = Arrangement.spacedBy(itemGap4),
) {
    PullRefresh(
        isRefreshing = pagingData.loadState.refresh is LoadState.Loading,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = contentArrangement
        ) {
            if (pagingData.loadState.refresh is LoadState.Loading) {
                repeat(5) {
                    item { loadingContent() }
                }
            }else{
                items(pagingData.itemCount) { index ->
                    pagingData[index]?.let {
                        itemContent(it)
                    }
                }
            }

            if (pagingData.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = theme.primary,
                            strokeWidth = 1.dp,
                            trackColor = theme.textFieldBackGround,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}