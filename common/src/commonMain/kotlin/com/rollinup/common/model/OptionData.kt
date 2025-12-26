package com.rollinup.common.model

data class OptionData<T>(
    val label: String,
    val value: T,
)