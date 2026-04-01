package com.rollinup.rollinup.component.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.rollinup.common.model.LocaleEnum
import java.util.Locale

actual object AppLocale {
    private val AppLocale = staticCompositionLocalOf { Locale.getDefault() }

    actual val current: LocaleEnum
        @Composable get() = AppLocale.current.toLanguageTag().let {
            LocaleEnum.fromValues(it)
        }

    @Composable
    actual infix fun provides(locale: LocaleEnum): ProvidedValue<*> {
        val newLocale = Locale.forLanguageTag(locale.value)
        Locale.setDefault(newLocale)
        return AppLocale provides newLocale
    }
}