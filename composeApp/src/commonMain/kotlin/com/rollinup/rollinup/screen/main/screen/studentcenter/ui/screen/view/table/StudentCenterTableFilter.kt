package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view.table

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
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.spacer.popupPadding
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.uistate.StudentCenterUiState
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun StudentCenterTableFilter(
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = theme.popUpBg, shape = RoundedCornerShape(12.dp))
            .padding(popupPadding),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier.width(120.dp)
        ) {
            SearchTextField(
                title = "Search",
                onValueChange = {},
                value = uiState.searchQuery,
                placeholder = "Search",
                onSearch = cb.onSearch
            )
        }
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                icon = Res.drawable.ic_print_line_24,
                onClick = {/*TODO ADD PRINT */ }
            )
            MultiDropDownSelector(
                title = "Class",
                value = uiState.filterData.classX,
                options = uiState.classOptions,
                onValueChange = {
                    cb.onFilter(
                        uiState.filterData.copy(
                            classX = it
                        )
                    )
                },
                placeHolder = "All"
            )

            MultiDropDownSelector(
                title = "Gender",
                value = uiState.filterData.gender,
                options = uiState.genderOptions,
                onValueChange = {
                    cb.onFilter(
                        uiState.filterData.copy(
                            gender = it
                        )
                    )
                },
                placeHolder = "All"
            )
        }
    }
}