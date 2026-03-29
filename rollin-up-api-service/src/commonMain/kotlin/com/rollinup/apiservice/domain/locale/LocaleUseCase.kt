package com.rollinup.apiservice.domain.locale

import com.rollinup.apiservice.data.repository.language.LocaleRepository
import com.rollinup.common.model.LocaleEnum

class SetLocaleUseCase(private val repository: LocaleRepository) {
    suspend operator fun invoke(locale: LocaleEnum) = repository.setLocale(locale)
}

class GetLocaleUseCase(private val repository: LocaleRepository) {
    suspend operator fun invoke() = repository.getLocale()
}