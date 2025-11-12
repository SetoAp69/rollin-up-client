package com.rollinup.apiservice.di

object IOSDataModule {
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