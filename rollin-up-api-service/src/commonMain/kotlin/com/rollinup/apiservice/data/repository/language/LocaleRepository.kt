package com.rollinup.apiservice.data.repository.language

import com.rollinup.common.model.LocaleEnum

interface LocaleRepository {
    suspend fun getLocale(): LocaleEnum
    suspend fun setLocale(locale: LocaleEnum)
}