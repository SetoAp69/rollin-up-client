package com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.table

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.filter.TableFilterRow
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterData
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterFilterOption
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view.CreateEditUserDialog
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.uistate.UserCenterUiState
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_plus_line_24

@Composable
fun UserCenterTableFilter(
    uiState: UserCenterUiState,
    onSearch: (String) -> Unit,
    onFilter: (UserCenterFilterData) -> Unit,
    onRefresh: () -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }

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
                onSearch = onSearch
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            icon = Res.drawable.ic_plus_line_24,
            severity = Severity.PRIMARY,
            onClick = {
                showCreateDialog = true
            }
        )
        UserCenterFilterRow(
            isLoading = uiState.isLoadingFilter,
            filterData = uiState.filterData,
            filterOptions = uiState.filterOptions,
            onFilter = onFilter
        )
    }

    CreateEditUserDialog(
        showDialog = showCreateDialog,
        onDismissRequest = { showCreateDialog = it },
        onSuccess = onRefresh
    )
}

@Composable
private fun UserCenterFilterRow(
    isLoading: Boolean,
    filterData: UserCenterFilterData,
    filterOptions: UserCenterFilterOption,
    onFilter: (UserCenterFilterData) -> Unit,
) {
    TableFilterRow(
        onReset = { onFilter(UserCenterFilterData()) },
        showReset = filterData != UserCenterFilterData(),
    ) {
        MultiDropDownSelector(
            title = "Class",
            placeHolder = "All",
            isLoading = isLoading,
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
            isLoading = isLoading,
            placeHolder = "All",
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
}