package com.rollinup.rollinup.component.pagination

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_left_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24

/**
 * A pagination control component that allows navigating through pages of data.
 *
 * It displays a set of page numbers and navigation arrows (previous/next). Smartly handles
 * truncation (ellipses) when the total number of pages exceeds the [showedPageCount].
 *
 * @param totalPage The total number of available pages.
 * @param currentPage The currently active page index (1-based).
 * @param showedPageCount The maximum number of page buttons to display at once. Defaults to 5.
 * @param onPageChange Callback triggered when a new page is selected.
 */
@Composable
fun Pagination(
    totalPage: Int,
    currentPage: Int,
    showedPageCount: Int = 5,
    onPageChange: (Int) -> Unit,
) {
    val showedPage = getShowedPage(totalPage, currentPage, showedPageCount)
    val showedPageCount = showedPageCount.coerceIn(1, totalPage)

    CustomRipple(theme.primary) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                icon = Res.drawable.ic_drop_down_arrow_line_left_24,
                onClick = {
                    onPageChange(currentPage - 1)
                },
                enabled = currentPage != 1,
                size = 16.dp
            )
            Spacer(4.dp)
            val hasHiddenItemOnLeft = currentPage >= showedPageCount && totalPage > showedPageCount
            if (hasHiddenItemOnLeft) {
                Text(
                    text = "1",
                    color = theme.bodyText,
                    style = Style.title,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            onPageChange(1)
                        }
                        .padding(horizontal = 8.dp)

                )
                Text(
                    text = "...",
                    style = Style.title,
                    color = theme.bodyText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            showedPage.forEach { page ->
                val isSelected = page == currentPage
                val background = if (isSelected) theme.popUpBgSelected else Color.Transparent
                val color = if (isSelected) theme.textPrimary else theme.bodyText
                Text(
                    text = "$page",
                    style = Style.title,
                    color = color,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(currentPage != page) {
                            onPageChange(page)
                        }
                        .background(color = background)
                        .padding(horizontal = 8.dp)

                )
            }

            val hasHiddenItemOnRight =
                currentPage <= totalPage - showedPageCount + 1 && totalPage > showedPageCount
            if (hasHiddenItemOnRight) {
                Text(
                    text = "...",
                    style = Style.title,
                    color = theme.bodyText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "$totalPage",
                    style = Style.title,
                    color = theme.bodyText,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            onPageChange(totalPage)
                        }
                        .padding(horizontal = 8.dp)

                )
            }
            Spacer(4.dp)
            IconButton(
                icon = Res.drawable.ic_drop_down_arrow_line_right_24,
                onClick = {
                    onPageChange(currentPage + 1)
                },
                enabled = currentPage != totalPage,
                size = 16.dp
            )
        }
    }
}

/**
 * Calculates the range of page numbers to display based on the current position.
 *
 * Logic:
 * 1. If total pages fit within the limit, show all.
 * 2. If near the start, show the first [showedPageCount] pages.
 * 3. If near the end, show the last [showedPageCount] pages.
 * 4. Otherwise, center the current page within the range.
 */
private fun getShowedPage(
    totalPage: Int,
    currentPage: Int,
    showedPageCount: Int,
): List<Int> {
    return when {
        totalPage <= showedPageCount -> {
            (1..totalPage).toList()
        }

        currentPage < showedPageCount -> {
            (1..showedPageCount).toList()
        }

        currentPage > totalPage - showedPageCount + 1 -> {
            (totalPage - showedPageCount + 1..totalPage).toList()
        }

        else -> {
//            (totalPage - showedPageCount / 2..totalPage + showedPageCount / 2).toList()
            ((currentPage - showedPageCount / 2) + 1..(currentPage + showedPageCount / 2) - 1).toList()
        }
    }
}