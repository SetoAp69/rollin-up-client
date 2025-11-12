package com.rollinup.apiservice.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

object DispatcherModule {
    operator fun invoke() = module {
        val dispatcher = Dispatchers.IO
        single<CoroutineDispatcher> { dispatcher }
    }
}