package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate.StudentPermitUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_permit

@Composable
fun StudentPermitTopBar(
    onNavigateUp: () -> Unit,
    uiState: StudentPermitUiState,
    cb: StudentPermitCallback,
) {
    val listMenu = listOf(Menu.FILTER)
    var showFilter by remember { mutableStateOf(false) }

    TopBar(
        title = stringResource(Res.string.label_permit),
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                else -> {}
            }
        },
        menu = listMenu,
        onNavigateUp = onNavigateUp,
        onSearch = cb.onSearch,
    )

    StudentPermitFilterSheet(
        isShowSheet = showFilter,
        onDismissRequest = { showFilter = it },
        onApply = cb.onFilter,
        uiState = uiState
    )
}