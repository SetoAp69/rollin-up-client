package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate

import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab

data class StudentPermitUiState(
    val user: LoginEntity = LoginEntity(),
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val searchQuery: String = "",
    val filterData: PermitFilterData = PermitFilterData(),
    val currentTab: PermitTab = PermitTab.ACTIVE,
    val cancelState: Boolean? = null,
) {
    val tabList
        get() = PermitTab.entries.map { it.title }

    val currentTabIndex
        get() = PermitTab.entries.indexOf(currentTab)

    val statusOptions
        get() = ApprovalStatus.entries.map {
            OptionData(it.label, it.value)
        }

    val typeOptions
        get() = PermitType.entries.map {
            OptionData(it.label, it.value)
        }

    val isActive
        get() = currentTab == PermitTab.ACTIVE

    val queryParams
        get() = GetPermitListQueryParams(
            search = searchQuery.ifBlank { null },
            isActive = (currentTab == PermitTab.ACTIVE).toString(),
            type = filterData.type.toJsonString(),
            dateRange = filterData.dateRange.toJsonString(),
            status = filterData.status.toJsonString(),
        )
}
