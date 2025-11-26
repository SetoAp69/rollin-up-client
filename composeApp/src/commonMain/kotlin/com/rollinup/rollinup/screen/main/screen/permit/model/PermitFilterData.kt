package com.rollinup.rollinup.screen.main.screen.permit.model

data class PermitFilterData(
    val dateRange: List<Long> = emptyList(),
    val status: List<String> = emptyList(),
    val type: List<String> = emptyList(),
)