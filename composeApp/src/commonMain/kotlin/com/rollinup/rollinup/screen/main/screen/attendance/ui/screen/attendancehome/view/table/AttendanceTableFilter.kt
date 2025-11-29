package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.common.utils.Utils.toLocalDate
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.date.SingleDatePicker
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.spacer.popupPadding
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.AttendanceSummary
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun AttendanceTableFilter(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
) {
    val filterData = uiState.filterData

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(12.dp),
                color = theme.popUpBg
            )
            .padding(popupPadding),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.width(320.dp)
            ) {
                SearchTextField(
                    title = "Search",
                    onValueChange = {},
                    value = uiState.searchQuery,
                    onSearch = cb.onSearch,
                    placeholder = "Enter keyword"
                )
            }
            Box(modifier = Modifier.width(320.dp)){
                AttendanceSummary(
                    isLoading = uiState.isLoadingSummary,
                    summary = uiState.summary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                icon = Res.drawable.ic_print_line_24,
            ) {
                //TODO:ADD PRINT FUNCTIONS
            }
            Box(modifier = Modifier.width(120.dp)) {
                SingleDatePicker(
                    title = "Date",
                    value = uiState.filterData.date.firstOrNull(),
                    onValueChange = { millis ->
                        val newValue =
                            if (millis == null) emptyList<Long>()
                            else {
                                val from = LocalDateTime(millis.toLocalDate(), LocalTime(0, 0, 0))
                                val to = LocalDateTime(millis.toLocalDate(), LocalTime(23, 59, 59))

                                listOf(from.toEpochMillis(), to.toEpochMillis())
                            }

                        cb.onFilter(
                            filterData.copy(
                                date = newValue
                            )
                        )
                    }
                )
            }
            SingleDropDownSelector(
                title = "Class",
                options = emptyList(),
                value = null,
                enable = false,
                placeHolder = uiState.user.classX ?: "-"
            ) {}
            MultiDropDownSelector(
                title = "Status",
                options = uiState.statusOptions,
                value = filterData.status,
                placeHolder = "All"
            ) {
                cb.onFilter(
                    filterData.copy(
                        status = it
                    )
                )
            }
        }
    }
}