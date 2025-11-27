package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.date.FilterDatePicker
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24


@Composable
fun TeacherPermitTableFilter(
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
) {
    val filterData = uiState.filterData
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = theme.popUpBg,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.width(320.dp)) {
            SearchTextField(
                title = "Search",
                onValueChange = {},
                value = uiState.searchQuery,
                onSearch = cb.onSearch,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            icon = Res.drawable.ic_print_line_24,
        ) {
            //TODO:SHOW PRINT DIALOG
        }
        Box(modifier = Modifier.width(145.dp)) {
            FilterDatePicker(
                title = "Date",
                value = filterData.dateRange,
                enabled = true,
                onValueChange = {
                    cb.onFilter(
                        filterData.copy(
                            dateRange = it
                        )
                    )
                }
            )
        }
        SingleDropDownSelector(
            title = "Class",
            value = uiState.user.classX ?: "-",
            options = emptyList(),
            onValueChange = {},
            enable = false
        )
        if (uiState.currentTab == PermitTab.ACTIVE) {
            MultiDropDownSelector(
                title = "Status",
                value = filterData.status,
                options = uiState.statusOptions,
                onValueChange = {
                    cb.onFilter(
                        filterData.copy(
                            status = it
                        )
                    )
                }
            )
        }
    }
}