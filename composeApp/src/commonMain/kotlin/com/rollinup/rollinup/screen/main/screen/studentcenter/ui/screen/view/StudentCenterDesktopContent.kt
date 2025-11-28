package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.uistate.StudentCenterUiState
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view.table.StudentCenterTable
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view.table.StudentCenterTableFilter

@Composable
fun StudentCenterDesktopContent(
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
) {
    Scaffold {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Student Center",
                style = Style.headerBold,
                color = theme.primary
            )
            StudentCenterTableFilter(
                uiState = uiState,
                cb = cb
            )
            StudentCenterTable(
                uiState = uiState
            )
        }
    }
}