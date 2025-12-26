package com.rollinup.rollinup.component.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_left_24
import rollin_up.composeapp.generated.resources.label_rows_per_page

// A fully customizable Jetpack Compose table component with:
// - Pagination
// - Page size selector (limit)
// - Customizable header (can insert any composable)
// - Row selection + Select All checkbox
// - Sticky header
// - Reusable, generic, and lightweight


// A fully customizable Jetpack Compose table component with:
// - Pagination
// - Page size selector (limit)
// - Customizable header (can insert any composable)
// - Row selection + Select All checkbox
// - Sticky header
// - Reusable, generic, and lightweight

@Composable
fun <T> AdvancedTable(
    items: List<T>,
    columns: List<TableColumn<T>>, // column definitions
    modifier: Modifier = Modifier,
    pageSizes: List<Int> = listOf(5, 10, 25, 50),
    headerContent: (@Composable RowScope.() -> Unit)? = null,
    onSelectionChange: (List<T>) -> Unit = {},
) {
    var currentPage by remember { mutableStateOf(0) }
    var selectedItems by remember { mutableStateOf(setOf<T>()) }
    var pageSize by remember { mutableStateOf(pageSizes.first()) }

    val pageCount = (items.size + pageSize - 1) / pageSize
    val pagedData = items.drop(currentPage * pageSize).take(pageSize)

    LazyColumn(modifier.fillMaxWidth()) {

        // --- Custom header area (filters, search, etc.) ---
        if (headerContent != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    content = headerContent
                )
            }
        }
        // --- Sticky Table Header ---
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF))
                    .padding(vertical = 8.dp),
            ) {
                val allSelected = pagedData.isNotEmpty() && pagedData.all { it in selectedItems }

                Checkbox(
                    checked = allSelected,
                    onCheckedChange = { checked ->
                        selectedItems =
                            if (checked) selectedItems + pagedData else selectedItems - pagedData.toSet()
                        onSelectionChange(selectedItems.toList())
                    }
                )

                columns.forEach { col ->
                    Text(
                        text = col.title,
                        modifier = Modifier.weight(col.weight).padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- Table Rows ---
        items(pagedData) { item ->
            val selected = item in selectedItems

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (selected) Color(0xFFEEF7FF) else Color.White)
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { checked ->
                        selectedItems = if (checked) selectedItems + item else selectedItems - item
                        onSelectionChange(selectedItems.toList())
                    }
                )

                columns.forEach { col -> col.content(item) }
            }
        }

        // --- Pagination ---
        item {
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Page Size Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.label_rows_per_page),
                        style = Style.body,
                        color = theme.bodyText
                    )
                    DropdownMenuLimitSelector(
                        options = pageSizes,
                        selected = pageSize,
                        onSelected = {
                            pageSize = it
                            currentPage = 0
                        }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (currentPage > 0) currentPage-- }) {
                        Icon(
                            painterResource(Res.drawable.ic_drop_down_arrow_line_left_24),
                            contentDescription = null
                        )
                    }
                    Text("${currentPage + 1} of $pageCount")
                    IconButton(onClick = { if (currentPage < pageCount - 1) currentPage++ }) {
                        Icon(
                            painterResource(Res.drawable.ic_drop_down_arrow_line_left_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

// Column definition


// Rows per page selector
@Composable
fun DropdownMenuLimitSelector(
    options: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = selected.toString(),
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}