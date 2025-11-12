package com.rollinup.apiservice.di

import com.rollinup.apiservice.source.datastore.IOSLocalDataStore

object LocalDataStoreModule {
    operator fun invoke() = module {
        single<LocalDataStore> {
            IOSLocalDataStore()
        }
    }
}