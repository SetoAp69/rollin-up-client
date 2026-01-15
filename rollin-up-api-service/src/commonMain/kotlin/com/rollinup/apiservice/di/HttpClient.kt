package com.rollinup.apiservice.di

import com.rollinup.apiservice.Constant
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ClientModule {
    operator fun invoke() =
        module {
            single<HttpClient>(named(Constant.HTTP_CLIENT)) { getHttpClient(get()) }
            single<HttpClient>(named(Constant.SSE_CLIENT)) { getSSEClient(get()) }
        }
}

expect fun getHttpClient(localDataStore: LocalDataStore): HttpClient

expect fun getSSEClient(localDataStore: LocalDataStore): HttpClient

