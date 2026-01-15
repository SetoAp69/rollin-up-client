package com.rollinup.rollinup.component.table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.ceil

class TableState<T>(
    val pageSizeOptions: List<Int>,
) {
    var currentPage by mutableStateOf(1)
    var pageSize by mutableStateOf(pageSizeOptions.first())

    fun updatePageSize(size: Int) {
        pageSize = size
    }

    fun nextPage() {
        currentPage++
    }

    fun previousPage() {
        if (currentPage > 0) currentPage--
    }

    fun updatePage(page: Int) {
        currentPage = page
    }

    fun getPagedData(items: List<T>): List<T> {
        return if (items.isEmpty()) {
            items
        } else {
            val from = (currentPage - 1) * pageSize
            val to =
                if (currentPage * pageSize > items.size) items.size else currentPage * pageSize
            items.slice(from until to)
        }
    }

    fun getTotalPage(items: List<T>): Int {
        val total = ceil((items.size - 1).toFloat() / pageSize).toInt()
        return if (total < 1) 1 else total
    }
}

@Composable
fun <T> rememberTableState(
    pageSizeOptions: List<Int> = listOf(25, 50, 100),
) = remember { TableState<T>(pageSizeOptions) }