package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState

@Composable
fun StudentCenterTopBar(
    onNavigateUp: () -> Unit,
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
) {
    var showFilter by remember { mutableStateOf(false) }
    TopBar(
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                else -> {}
            }
        },
        menu = listOf(Menu.FILTER),
        title = "Student Center",
        onNavigateUp = onNavigateUp
    )

    StudentCenterFilterSheet(
        showFilter = showFilter,
        onDismissRequest = { showFilter = it },
        onApply = cb.onFilter,
        uiState = uiState
    )
}