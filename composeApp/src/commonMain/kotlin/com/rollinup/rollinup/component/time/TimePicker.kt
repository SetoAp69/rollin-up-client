@file:OptIn(ExperimentalTime::class)

package com.rollinup.rollinup.component.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.common.utils.Utils.now
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.LocalTime
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime


@Composable
fun TimePicker(
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    selectedBackgroundColor: Color = theme.popUpBgSelected,
    selectedColor: Color = theme.bodyText,
    textColor: Color = selectedColor.copy(alpha = 0.6f),
    selectedTextStyle: TextStyle = Style.popupTitle,
    textStyle: TextStyle = Style.popupTitle,
    min: LocalTime? = null,
    max: LocalTime? = null,
) {
    var rowHeight by remember { mutableStateOf(0.dp) }
    val listMinute = (0..59).map { it }
    val listHour = (0..23).map { it }
    val selectedColor = theme.bodyText
    val textColor = selectedColor.copy(alpha = 0.6f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight),
        verticalAlignment = Alignment.CenterVertically

    ) {
        SnapList(
            itemList = listHour,
            modifier = Modifier.weight(1f),
            value = value.hour,
            onHeightMeasured = {
                rowHeight = it
            },
            onValueChange = { hour ->
                val newValue = LocalTime(hour = hour, minute = value.minute, second = 0)
                onValueChange(newValue)
            }
        ) { hour ->
            if (hour == value.hour) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(itemGap8)
                        .background(selectedBackgroundColor),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = hour.toString(),
                        style = selectedTextStyle,
                        color = selectedColor
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(itemGap8)
                    ,
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = hour.toString(),
                        style = textStyle,
                        color = textColor
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(selectedBackgroundColor)
                .padding(horizontal = itemGap8)
//                .padding(itemGap8)
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ":",
                style = selectedTextStyle,
                color = selectedColor
            )
        }
        SnapList(
            itemList = listHour,
            modifier = Modifier.weight(1f),
            value = value.minute,
            onValueChange = { minute ->
                val newValue = LocalTime(hour = value.hour, minute = minute, second = 0)
                onValueChange(newValue)
            }
        ) { minute ->
            if (minute == value.minute) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(itemGap8)
                        .background(selectedBackgroundColor),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = minute.toString(),
                        style = selectedTextStyle,
                        color = selectedColor
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(itemGap8)
                    ,
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = minute.toString(),
                        style = textStyle,
                        color = textColor
                    )
                }
            }
        }

    }
}


@Composable
fun SnapListTest() {
    val items = (0..3).map { it }
    var value by remember { mutableStateOf(LocalTime.now()) }

    TimePicker(
        value = value,
        onValueChange = {
            value = it
        }
    )
}


@Composable
fun TimePickerContent() {

}

//@Composable
//fun SnapList(
//    itemList: List<Int>,
//    modifier: Modifier = Modifier,
//    itemGap: Dp = itemGap8,
//    value: Int,
//    onValueIndexChange: (Int) -> Unit,
//    contentPadding: PaddingValues = screenPadding,
//    content: @Composable ColumnScope.(Int) -> Unit,
//) {
//    val coroutineScope = rememberCoroutineScope()
//    var height by remember { mutableStateOf(12.dp) }
//    var listHeight by remember { mutableStateOf(200.dp) }
//    val localDensity = LocalDensity.current
//    var visibleItemSize by remember { mutableStateOf(0) }
//    var currentFirstVisibleIndex by remember { mutableStateOf(0) }
//    var currentValueIndex by remember { mutableStateOf(0) }
//
//    LaunchedEffect(value) {
//        val currentValueIndex = itemList.indexOf(value)
//        if (currentValueIndex != -1) {
//            currentFirstVisibleIndex =calculateCenterVisibleIndex(currentValueIndex,visibleItemSize)
//        }
//    }
//
//    val listState = rememberLazyListState(
//        initialFirstVisibleItemIndex = currentFirstVisibleIndex,
//    )
//    L.wtf { listState.toString() }
//
//    LazyColumn(
//        state = listState,
//        modifier = modifier
//            .height(listHeight),
//        verticalArrangement = Arrangement.spacedBy(itemGap),
//        contentPadding = contentPadding
//    ) {
//        items(itemList) { item ->
//            Column(
//                modifier = Modifier
//                    .onGloballyPositioned {
//                        with(localDensity) {
//                            height = ((it.size.height + itemGap.value) / density).dp
//                            listHeight =
//                                (((it.size.height + itemGap.value) / density).dp + (2 * contentPadding.calculateTopPadding().value / density).dp) * 5
//                        }
//                    }
//            ) {
//                content(item)
//            }
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        snapshotFlow { listState.isScrollInProgress }
//            .collect { isScrolling ->
//                visibleItemSize = listState.layoutInfo.visibleItemsInfo.size
//
//                if (!isScrolling) {
//                    val lastItemIndex = itemList.size - 1
//                    val layoutInfo = listState.layoutInfo
//                    val isLastItemVisible =
//                        layoutInfo.visibleItemsInfo.any { it.index == lastItemIndex }
//                    if (!isLastItemVisible) {
//                        val targetIndex = calculateTargetIndex(
//                            listState.firstVisibleItemIndex,
//                            listState.firstVisibleItemScrollOffset,
//                            with(localDensity) { (height).roundToPx() }.toFloat(),
//                            itemList.size
//                        )
//                        currentValueIndex = calculateCenterVisibleIndex(
//                            firstVisibleItemIndex = listState.firstVisibleItemIndex,
//                            visibleItemSize = layoutInfo.visibleItemsInfo.size
//                        )
//                        coroutineScope.launch {
//                            listState.animateScrollToItem(index = targetIndex)
//                        }
//                    }
//                }
//            }
//    }
//
//    LaunchedEffect(currentValueIndex) {
//        onValueIndexChange(currentValueIndex)
//    }
//}

@Composable
fun SnapList(
    itemList: List<Int>,
    modifier: Modifier = Modifier,
    itemGap: Dp = 8.dp,
    value: Int,
    centerOffset: Int = 2,
    onValueChange: (Int) -> Unit,
    onHeightMeasured: (Dp) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    content: @Composable ColumnScope.(Int) -> Unit,
) {
    val currentItemList = List(20) { itemList }.flatten()
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    var itemHeightPx by remember { mutableStateOf(0) }
    val selectedIndex = itemList.indexOf(value).coerceAtLeast(0) + itemList.size * 10

    val itemModifier = if (itemHeightPx == 0) {
        Modifier.onGloballyPositioned {
            itemHeightPx = it.size.height
        }
    } else Modifier

    LaunchedEffect(itemHeightPx) {
        if (itemHeightPx > 0 && selectedIndex >= 0) {
            val target = (selectedIndex - centerOffset).coerceAtLeast(0)
            listState.scrollToItem(target)
        }
    }

    LaunchedEffect(listState.isScrollInProgress, itemHeightPx) {
        if (!listState.isScrollInProgress && itemHeightPx > 0) {
            val centerIndex = computeCenterIndex(
                itemHeightPx = itemHeightPx,
                listState = listState,
                itemList = currentItemList,
                centerOffset = centerOffset
            )
            val newValue = itemList.getOrNull(centerIndex % itemList.size)
            var target = centerIndex - 2
            val upperThreshold = itemList.size * 15
            val lowerThreshold = itemList.size * 5

            when {
                target > upperThreshold || target < lowerThreshold -> {
                    target = centerIndex % itemList.size - centerOffset + itemList.size * 10
                    listState.scrollToItem(target)
                }

                else -> {
                    listState.animateScrollToItem(centerIndex - centerOffset)
                }

            }

            newValue?.let {
                if (it != value) {
                    onValueChange(it)
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.height(
            with(density) {
                val height =
                    (itemHeightPx * 5).toDp() + itemGap * 5 + contentPadding.calculateTopPadding() * 2
                onHeightMeasured(height)
                height
            }
        ),
        verticalArrangement = Arrangement.Center,
        contentPadding = contentPadding
    ) {
        items(currentItemList.size) {
            Column(modifier = itemModifier) {
                val index = it % currentItemList.size
                content(currentItemList[index])
            }
            Spacer(modifier = Modifier.height(itemGap))
        }
    }
}

fun computeCenterIndex(
    itemHeightPx: Int,
    listState: LazyListState,
    itemList: List<Int>,
    centerOffset: Int,
): Int {
    if (itemHeightPx == 0) return 0

    val first = listState.firstVisibleItemIndex
    val offset = listState.firstVisibleItemScrollOffset

    val fractional = offset / itemHeightPx.toFloat()
    val centerIndex = (first + fractional.roundToInt() + centerOffset)

    return centerIndex.coerceIn(0, itemList.lastIndex)
}

private fun calculateCenterVisibleIndex(
    firstVisibleItemIndex: Int,
    visibleItemSize: Int,
): Int {
    return firstVisibleItemIndex + (floor(visibleItemSize.toDouble() / 2)).toInt() - 1
}

private fun calculateTargetIndex(
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
    itemWidthPx: Float,
    itemCount: Int,
): Int {
    val totalScrollOffset = firstVisibleItemIndex * itemWidthPx + firstVisibleItemScrollOffset
    var targetIndex = (totalScrollOffset / itemWidthPx).toInt()

    val visibleItemFraction = totalScrollOffset % itemWidthPx
    if (visibleItemFraction > itemWidthPx / 2) {
        targetIndex++
    }

    if (targetIndex >= itemCount - 1) {
        targetIndex = itemCount - 1
    }

    return targetIndex
}
