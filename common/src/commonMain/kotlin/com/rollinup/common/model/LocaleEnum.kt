package com.rollinup.common.model

enum class LocaleEnum(
    val value: String,
    val label: String,
) {
    EN(value = "en", "English"),
    IN(value = "in", label = "Bahasa Indonesia");

    companion object {
        fun getOptions() =
            entries.map {
                OptionData(it.label, it)
            }

        fun fromValues(value: String) =
            entries.find { it.value == value } ?: IN
    }
}