package com.rollinup.apiservice.di

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