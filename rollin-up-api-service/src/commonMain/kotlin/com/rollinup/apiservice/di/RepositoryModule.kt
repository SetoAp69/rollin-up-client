package com.rollinup.apiservice.di

import com.rollinup.apiservice.data.repository.auth.AuthRepository
import com.rollinup.apiservice.data.repository.auth.AuthRepositoryImpl
import com.rollinup.apiservice.data.repository.generalsetting.GeneralSettingRepository
import com.rollinup.apiservice.data.repository.generalsetting.GeneralSettingRepositoryImpl
import com.rollinup.apiservice.data.repository.pagging.PagingDummyRepository
import com.rollinup.apiservice.data.repository.pagging.PagingDummyRepositoryImpl
import com.rollinup.apiservice.data.repository.user.UserRepository
import com.rollinup.apiservice.data.repository.user.UserRepositoryImpl
import org.koin.dsl.module

object RepositoryModule {
    operator fun invoke() = module {
        single<AuthRepository> {
            AuthRepositoryImpl(
                dataSource = get(),
                ioDispatcher = get(),
                mapper = get()
            )
        }
        single<UserRepository> {
            UserRepositoryImpl(
                userApiService = get(),
                mapper = get(),
                ioDispatcher = get()
            )
        }

        single<GeneralSettingRepository> {
            GeneralSettingRepositoryImpl(
                dataSource = get(),
                ioDispatcher = get(),
                mapper = get()
            )
        }

        single<PagingDummyRepository> {
            PagingDummyRepositoryImpl(
                dataSource = get(),
                ioDispatcher = get()
            )
        }

    }
}