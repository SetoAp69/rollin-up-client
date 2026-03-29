package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.domain.locale.GetLocaleUseCase
import com.rollinup.apiservice.domain.locale.SetLocaleUseCase
import com.rollinup.common.model.LocaleEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocaleViewModel(
    private val getLocaleUseCase: GetLocaleUseCase,
    private val setLocaleUseCase: SetLocaleUseCase,
) : ViewModel() {
    private var _locale = MutableStateFlow(LocaleEnum.IN)
    val locale = _locale.asStateFlow()

    fun getLocale() {
        viewModelScope.launch {
            _locale.update { getLocaleUseCase() }
        }
    }

    fun setLocale(locale: LocaleEnum) {
        viewModelScope.launch {
            _locale.update { locale }
            setLocaleUseCase(locale)
            getLocale()
        }
    }
}