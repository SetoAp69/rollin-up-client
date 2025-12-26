package com.rollinup.apiservice.di

import com.rollinup.apiservice.data.source.datastore.JVMLocalDataStore
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import org.koin.dsl.module

object LocalDataStoreModule {
    operator fun invoke() = module {
        single<LocalDataStore> {
            JVMLocalDataStore()
        }
    }
}