package com.rollinup.rollinup.component.model

import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_arrow_left_line_24
import rollin_up.composeapp.generated.resources.ic_filter_line_24
import rollin_up.composeapp.generated.resources.ic_more_fill_24
import rollin_up.composeapp.generated.resources.ic_print_line_24
import rollin_up.composeapp.generated.resources.ic_search_line_24
import rollin_up.composeapp.generated.resources.ic_select_all_line_24
import rollin_up.composeapp.generated.resources.ic_unselect_all_fill_24

enum class Menu(
    val title: String,
    val icon: DrawableResource,
    val iconFilled: DrawableResource = icon,
) {
    SEARCH(
        title = "Search",
        icon = Res.drawable.ic_search_line_24
    ),
    FILTER(
        title = "Filter",
        icon = Res.drawable.ic_filter_line_24
    ),
    PRINT(
        title = "Print",
        icon = Res.drawable.ic_print_line_24
    ),
    BACK(
        title = "Back",
        icon = Res.drawable.ic_arrow_left_line_24
    ),
    SELECT_ALL(
        title = "Select All",
        icon = Res.drawable.ic_select_all_line_24
    ),
    UNSELECT_ALL(
        title = "Unselect All",
        icon = Res.drawable.ic_unselect_all_fill_24
    ),
    ACTION(
        title = "Action",
        icon = Res.drawable.ic_more_fill_24
    )
}