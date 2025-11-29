package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.AttendanceByStudentSummary
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun AttendanceByStudentTableFilter(
    uiState: AttendanceByStudentUiState,
    onStatusSelected: (List<AttendanceStatus>) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(400.dp)
                .padding(horizontal = 12.dp)
        ) {
            AttendanceByStudentSummary(
                isLoading = uiState.isLoadingSummary,
                summary = uiState.summary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            icon = Res.drawable.ic_print_line_24
        ) {
            //TODO: ADD PRINT FUNCTIONS
        }
        MultiDropDownSelector(
            title = "Status",
            options = uiState.statusOptions,
            value = uiState.statusSelected,
            onValueChange = onStatusSelected
        )
    }
}