package com.rollinup.apiservice.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

object AndroidDataModule {
    operator fun invoke() = listOf(
        LocalDataStoreModule(),
        ClientModule(),
        DatasourceModule(),
        RepositoryModule(),
        MapperModule(),
        DispatcherModule(),
        DomainModule()
    )
}