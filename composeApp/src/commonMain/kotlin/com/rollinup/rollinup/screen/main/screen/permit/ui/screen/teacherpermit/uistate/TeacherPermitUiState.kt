package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate

import com.rollinup.apiservice.data.source.network.model.request.permit.GetPermitListQueryParams
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab

data class TeacherPermitUiState(
    val user: LoginEntity = LoginEntity(),
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val itemList: List<PermitByClassEntity> = emptyList(),
    val itemSelected: List<PermitByClassEntity> = emptyList(),
    val searchQuery: String = "",
    val currentTab: PermitTab = PermitTab.ACTIVE,
    val filterData: PermitFilterData = PermitFilterData(),
    val exportState: Boolean? = null,
    val isMobile: Boolean = false,
) {
    val statusOptions
        get() = ApprovalStatus
            .entries
            .filter { it != ApprovalStatus.APPROVAL_PENDING }
            .map { OptionData(it.label, it.name) }

    val typeOptions
        get() = PermitType
            .entries
            .map {
                OptionData(it.label, it.value)
            }

    val queryParams
        get() = GetPermitListQueryParams(
            search = searchQuery.ifBlank { null },
            isActive = (currentTab == PermitTab.ACTIVE).toString(),
            type = filterData.type.toJsonString(),
            dateRange = filterData.dateRange.toJsonString(),
            status = filterData.status.toJsonString(),
        )

    val isAllSelected
        get() = itemSelected.size == itemList.size && itemSelected.isNotEmpty()

    val tabList
        get() = PermitTab.entries.map { it.title }

    val currentTabIndex: Int
        get() = PermitTab.entries.indexOf(currentTab)
}

