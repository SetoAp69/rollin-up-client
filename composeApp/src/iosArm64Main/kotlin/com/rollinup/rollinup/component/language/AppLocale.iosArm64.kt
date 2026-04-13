package com.rollinup.rollinup.component.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import com.rollinup.common.model.LocaleEnum

actual object AppLocale {
    actual val current: LocaleEnum
        get() = TODO("Not yet implemented")

    @Composable
    actual infix fun provides(locale: LocaleEnum): ProvidedValue<*> {
        TODO("Not yet implemented")
    }
}