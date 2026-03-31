package com.rollinup.rollinup.component.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.rollinup.common.model.LocaleEnum
import java.util.Locale

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppLocale {
    private var localeDefault: LocaleEnum? = null

    actual val current: LocaleEnum
        @Composable
        get() = localeDefault ?: LocaleEnum.IN

    @Composable
    actual infix fun provides(locale: LocaleEnum): ProvidedValue<*> {
        val configuration = LocalConfiguration.current

        if (localeDefault == null) {
            localeDefault = Locale.getDefault().language.let {
                LocaleEnum.fromValues(it)
            }
        }
        localeDefault = locale
        val newLocale = Locale.forLanguageTag(locale.value)

        Locale.setDefault(newLocale)
        configuration.setLocale(newLocale)

        val activityContext = LocalContext.current
        val rawLocalizationContext = activityContext.createConfigurationContext(configuration)

        val safeContext = remember(rawLocalizationContext) {
            LocaleActivityWrapper(
                baseActivity = activityContext,
                localizedContext = rawLocalizationContext
            )
        }

        return LocalContext provides safeContext
    }
}
