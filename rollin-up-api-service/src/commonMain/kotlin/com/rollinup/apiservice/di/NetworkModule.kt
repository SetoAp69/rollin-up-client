package com.rollinup.apiservice.di

object NetworkModule {
    operator fun invoke() = listOf(
        ClientModule(),
        DatasourceModule(),
        RepositoryModule(),
        MapperModule(),
        DispatcherModule(),
    ) + DomainModule()
}