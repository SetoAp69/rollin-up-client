package com.rollinup.rollinup.component.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import com.rollinup.common.model.LocaleEnum

expect object AppLocale {
    val current: LocaleEnum @Composable get

    @Composable
    infix fun provides(locale: LocaleEnum): ProvidedValue<*>
}