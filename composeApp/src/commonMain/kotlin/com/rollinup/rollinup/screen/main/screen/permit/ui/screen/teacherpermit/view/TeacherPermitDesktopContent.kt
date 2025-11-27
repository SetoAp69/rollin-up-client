package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.table.TeacherPermitTableContent
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.table.TeacherPermitTableFilter

@Composable
fun TeacherPermitDesktopContent(
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
) {
    Scaffold {
        Column(
            modifier=Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Teacher Permit",
                style = Style.headerBold,
                color = theme.bodyText
            )
            TeacherPermitTableFilter(
                uiState = uiState,
                cb = cb
            )
            TeacherPermitTableContent(
                uiState = uiState,
                cb = cb
            )
        }
    }
}