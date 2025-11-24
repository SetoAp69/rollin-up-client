package com.rollinup.rollinup.component.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.rollinup.rollinup.component.checkbox.CheckBox
import com.rollinup.rollinup.component.checkbox.CheckBoxDefaults
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownState
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.pagination.Pagination
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_more_fill_24

@Composable
fun <T> Table(
    items: List<T>,
    columns: List<TableColumn<T>>,
    isLoading: Boolean,
    headerContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 12.dp,
    verticalPadding: Dp = 4.dp,
    showSelection: Boolean = false,
    showActionMenu: Boolean = false,
    tableState: TableState<T> = rememberTableState(),
    dropDownMenu: @Composable (DropDownState<T>) -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                color = theme.popUpBg,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .fillMaxSize()
    ) {
        TableHeader(
            tableState = tableState,
            pageCount = tableState.getTotalPage(items),
            content = headerContent
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = theme.primary,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = verticalPadding),

                    ) {
                    if (showSelection) {
                        CheckBox(
                            colors = CheckBoxDefaults.colors.copy(
                                uncheckedBoxColor = theme.popUpBg
                            ),
                            checked = tableState.selectedItem.size == items.size && items.isNotEmpty(),
                            onCheckedChange = {
                                if (it) {
                                    tableState.toggleSelectAll(items)
                                } else {
                                    tableState.toggleSelectAll(emptyList())
                                }
                            }
                        )
                    }
                    columns.fastForEach { col ->
                        Text(
                            text = col.title,
                            modifier = Modifier
                                .weight(col.weight)
                                .padding(horizontal = itemGap8),
                            style = Style.title,
                            textAlign = TextAlign.Center,
                            color = theme.textBtnPrimary
                        )
                    }
                    if (showActionMenu) {
                        TableActionButton(
                            showButton = tableState.selectedItem.isNotEmpty(),
                            items = tableState.selectedItem,
                            dropDownMenu = dropDownMenu,
                            color = theme.textBtnPrimary
                        )
                    }
                }
            }

            if (!isLoading) {
                items(tableState.getPagedData(items)) { item ->
                    val isSelected = item in tableState.selectedItem
                    TableRow(
                        item = item,
                        columns = columns,
                        showSelection = showSelection,
                        showActionMenu = showActionMenu,
                        isSelected = isSelected,
                        tableState = tableState,
                        horizontalPadding = horizontalPadding,
                        verticalPadding = verticalPadding,
                        dropDownMenu = dropDownMenu
                    )
                }
            } else {
                items(5) {
                    TableRowLoading(
                        columns = columns,
                        showSelection = showSelection,
                        showActionMenu = showActionMenu,
                        horizontalPadding = horizontalPadding,
                        verticalPadding = verticalPadding
                    )
                }
            }
        }
    }
}

@Composable
fun <T> TableRowLoading(
    columns: List<TableColumn<T>>,
    showSelection: Boolean,
    showActionMenu: Boolean,
    horizontalPadding: Dp,
    verticalPadding: Dp,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSelection) {
                CheckBox(
                    checked = false,
                    onCheckedChange = {}
                )
            }

            columns.fastForEach { col ->
                Box(
                    modifier = Modifier.weight(col.weight),
                    contentAlignment = Alignment.Center
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .height(16.dp)
                            .fillMaxWidth(0.8f)
                    )
                }
            }

            if (showActionMenu) {
                TableActionButton(
                    items = emptyList<T>(),
                    dropDownMenu = {},
                    color = theme.primary,
                    showButton = false
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
    }
}

@Composable
fun <T> TableRow(
    item: T,
    columns: List<TableColumn<T>>,
    showSelection: Boolean,
    showActionMenu: Boolean,
    isSelected: Boolean,
    tableState: TableState<T>,
    horizontalPadding: Dp = 12.dp,
    verticalPadding: Dp = 4.dp,
    dropDownMenu: (@Composable (DropDownState<T>) -> Unit),
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSelection) {
                CheckBox(
                    checked = isSelected,
                    onCheckedChange = {
                        tableState.toggleSelection(item)
                    }
                )
            }

            columns.fastForEach { col ->

                Box(
                    modifier = Modifier.weight(col.weight),
                    contentAlignment = Alignment.Center
                ) {

                    col.content(item)

                }

            }

            if (showActionMenu) {
                TableActionButton(
                    items = listOf(item),
                    dropDownMenu = dropDownMenu,
                    color = theme.primary,
                    showButton = tableState.selectedItem.isEmpty()
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
    }
}

@Composable
fun <T> TableHeader(
    tableState: TableState<T>,
    pageCount: Int,
    content: @Composable (RowScope.() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = itemGap8),
        verticalAlignment = Alignment.Bottom
    ) {
        content?.let {
            it.invoke(this)
            Spacer(itemGap8)
        }
        Spacer(modifier = Modifier.weight(1f))
        RowsPerPage(
            options = tableState.pageSizeOptions,
            selected = tableState.pageSize,
            onSelected = {
                tableState.updatePageSize(it)
            }
        )
        Spacer(itemGap8)
        Pagination(
            totalPage = pageCount,
            currentPage = tableState.currentPage,
            onPageChange = { tableState.updatePage(it) }
        )

    }
}

@Composable
fun <T> TableActionButton(
    items: List<T>,
    color: Color,
    size: Dp = 24.dp,
    showButton: Boolean = true,
    dropDownMenu: @Composable (DropDownState<T>) -> Unit,
) {
    Box(modifier = Modifier.size(size)) {
        var showDropDown by remember { mutableStateOf(false) }
        if (showButton) {
            ActionButton(
                size = size,
                onClickAction = { showDropDown = true },
                color = color,
            )
            dropDownMenu(DropDownState(items, showDropDown, { showDropDown = it }))
        }
    }
}

@Composable
fun <T> Table(
    items: List<T>,
    columns: List<TableColumn<T>>,
    headerContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    showSelection: Boolean = false,
    showActionMenu: Boolean = false,
    itemSelected: List<T> = emptyList(),
    onUpdateSelection: (T) -> Unit = {},
    onToggleSelectAll: (List<T>) -> Unit = {},
    pageSizeOptions: List<Int> = listOf(5, 10, 25, 50),
    dropDownMenu: @Composable (List<T>, Boolean, (Boolean) -> Unit) -> Unit,
    onClickAction: () -> Unit,
) {
    var currentPage by remember { mutableStateOf(0) }
    var pageSize by remember { mutableStateOf(pageSizeOptions.first()) }

    val pagedData = items.drop(currentPage * pageSize).take(pageSize)

    Column(
        modifier = modifier
            .background(
                color = theme.popUpBg,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.weight(1f)) {
                headerContent?.invoke(this)
                Spacer(itemGap8)
                RowsPerPage(
                    options = pageSizeOptions,
                    selected = pageSize,
                    onSelected = {
                        pageSize = it
                    }
                )
                Spacer(itemGap8)
                Pagination(
                    totalPage = items.size - 1,
                    currentPage = currentPage,
                    onPageChange = { currentPage = it }
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(itemGap4),
        ) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = theme.primary,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                        .fillMaxWidth()
                        .padding(12.dp),

                    ) {
                    if (showSelection) {
                        CheckBox(
                            checked = itemSelected.size == items.size,
                            onCheckedChange = {
                                if (it) {
                                    onToggleSelectAll(items)
                                } else {
                                    onToggleSelectAll(emptyList())
                                }
                            }
                        )
                    }
                    columns.fastForEach { col ->
                        Text(
                            text = col.title,
                            modifier = Modifier
                                .weight(col.weight)
                                .padding(horizontal = itemGap8),
                            style = Style.title,
                            color = theme.textBtnPrimary
                        )
                    }
                    if (itemSelected.isNotEmpty() && showActionMenu) {
                        Box {
                            var showDropDown by remember { mutableStateOf(false) }
                            ActionButton(
                                onClickAction = onClickAction,
                                color = theme.textBtnPrimary,
                            )
                            dropDownMenu(itemSelected, showDropDown, { showDropDown = it })
                        }
                    }
                }
            }

            items(pagedData) { item ->
                val isSelected = item in itemSelected
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (showSelection) {
                        CheckBox(
                            checked = isSelected,
                            onCheckedChange = {
                                onUpdateSelection(item)
                            }
                        )
                    }

                    columns.fastForEach { col ->
                        col.content(item)
                    }

                    if (itemSelected.isEmpty() && showActionMenu) {
                        Box {
                            var showDropDown by remember { mutableStateOf(false) }
                            ActionButton(
                                onClickAction = onClickAction,
                                color = theme.textBtnPrimary,
                            )
                            dropDownMenu(itemSelected, showDropDown, { showDropDown = it })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowsPerPage(
    options: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
) {
    var showDropDown by remember { mutableStateOf(false) }

    RecordField(
        title = "Rows per page",
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Text(
                text = selected.toString(),
                style = Style.title,
                color = theme.primary,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .clickable {
                        showDropDown = true
                    }
                    .background(
                        color = theme.secondary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp)
            )
            DropDownMenu(
                isShowDropDown = showDropDown,
                onDismissRequest = { showDropDown = it },
            ) {
                options.fastForEach { page ->
                    Text(
                        modifier = Modifier
                            .clickable {
                                onSelected(page)
                                showDropDown = false
                            }
                            .padding(itemGap4)
                            .width(32.dp),
                        text = page.toString(),
                        style = Style.body,
                        textAlign = TextAlign.Center,
                        color = theme.bodyText
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    onClickAction: () -> Unit,
    color: Color = theme.primary,
    size: Dp = 24.dp,
) {
    IconButton(
        modifier = Modifier.size(size),
        onClick = { onClickAction() },
        content = {
            Icon(
                modifier = Modifier.rotate(90f),
                painter = painterResource(Res.drawable.ic_more_fill_24),
                tint = color,
                contentDescription = null
            )
        }
    )
}

class TableColumn<T>(
    val title: String,
    val weight: Float = 1f,
    val content: @Composable (T) -> Unit,
)



