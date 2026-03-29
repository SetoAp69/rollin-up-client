package com.rollinup.apiservice.data.repository.language

import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.common.model.LocaleEnum

class LocaleRepositoryImpl(
    val localDataStore: LocalDataStore,
) : LocaleRepository {
    override suspend fun getLocale(): LocaleEnum {
        return localDataStore.getLocale()
    }

    override suspend fun setLocale(locale: LocaleEnum) {
        localDataStore.setLocale(locale)
    }
}