package com.rollinup.apiservice.di

object JVMDataModule {
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