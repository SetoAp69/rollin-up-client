package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState

@Composable
fun AttendanceListHeader(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenPaddingValues)
        ,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Attendance",
                style = Style.label,
                color = theme.bodyText
            )

            Chip(
                text = uiState.user?.classX ?: "-",
                severity = Severity.SECONDARY
            )
        }
        Spacer(itemGap8)
        Row {
            val listMenu = buildList {
                if (uiState.itemSelected.isNotEmpty()) {
                    if (uiState.isAllSelected) {
                        add(Menu.SELECT_ALL)
                    } else {
                        add(Menu.UNSELECT_ALL)
                    }
                }
                add(Menu.FILTER)
            }
            TopBar(
                onClickMenu = {

                },
                menu = listMenu,
                onSearch = {
                    cb.onUpdateFilter(
                        uiState.filterData.copy(
                            searchQuery = it
                        )
                    )
                },
                onNavigateUp = {},
                title = "",
            )
        }
        HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
    }
}