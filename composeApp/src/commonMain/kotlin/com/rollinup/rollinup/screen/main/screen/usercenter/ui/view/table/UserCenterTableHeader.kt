package com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterOption
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.uistate.UserCenterUiState

@Composable
fun UserCenterTableHeader(
    uiState: UserCenterUiState,
    onSearch: (String) -> Unit,
    onFilter: (UserCenterFilterData) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = theme.popUpBg
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.width(320.dp)) {
            SearchTextField(
                title = "Search",
                onValueChange = {},
                value = uiState.searchQuery,
                onSearch = onSearch
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        UserCenterTableFilter(
            filterData = uiState.filterData,
            filterOptions = uiState.filterOptions,
            onFilter = onFilter
        )
    }
}

@Composable
fun UserCenterTableFilter(
    filterData: UserCenterFilterData,
    filterOptions: UserCenterFilterOption,
    onFilter: (UserCenterFilterData) -> Unit,
) {
    if (filterData != UserCenterFilterData()) {
        Text(
            text = "Reset",
            color = theme.textPrimary,
            style = Style.body,
            modifier = Modifier.clickable() {
                onFilter(UserCenterFilterData())
            }
        )
    }

    MultiDropDownSelector(
        title = "Class",
        placeHolder = "All",
        value = filterData.classKey,
        options = filterOptions.classOptions,
        onValueChange = {
            onFilter(
                filterData.copy(
                    classKey = it
                )
            )
        }
    )

    MultiDropDownSelector(
        title = "Role",
        value = filterData.role,
        options = filterOptions.roleOptions,
        onValueChange = {
            onFilter(
                filterData.copy(
                    role = it
                )
            )
        }
    )

    MultiDropDownSelector(
        title = "Gender",
        placeHolder = "All",
        value = filterData.gender,
        options = filterOptions.genderOptions,
        onValueChange = {
            onFilter(
                filterData.copy(
                    gender = it
                )
            )
        }
    )
}