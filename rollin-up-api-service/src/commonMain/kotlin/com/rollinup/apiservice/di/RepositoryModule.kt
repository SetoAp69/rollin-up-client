package com.rollinup.apiservice.di

import com.rollinup.apiservice.data.repository.attendance.AttendanceRepository
import com.rollinup.apiservice.data.repository.attendance.AttendanceRepositoryImpl
import com.rollinup.apiservice.data.repository.auth.AuthRepository
import com.rollinup.apiservice.data.repository.auth.AuthRepositoryImpl
import com.rollinup.apiservice.data.repository.generalsetting.GlobalSettingRepository
import com.rollinup.apiservice.data.repository.generalsetting.GlobalSettingRepositoryImpl
import com.rollinup.apiservice.data.repository.pagging.PagingDummyRepository
import com.rollinup.apiservice.data.repository.pagging.PagingDummyRepositoryImpl
import com.rollinup.apiservice.data.repository.permit.PermitRepository
import com.rollinup.apiservice.data.repository.permit.PermitRepositoryImpl
import com.rollinup.apiservice.data.repository.token.TokenRepository
import com.rollinup.apiservice.data.repository.token.TokenRepositoryImpl
import com.rollinup.apiservice.data.repository.uimode.UiModeRepository
import com.rollinup.apiservice.data.repository.uimode.UiModeRepositoryImpl
import com.rollinup.apiservice.data.repository.user.UserRepository
import com.rollinup.apiservice.data.repository.user.UserRepositoryImpl
import org.koin.dsl.module

object RepositoryModule {
    operator fun invoke() = module {
        single<AuthRepository> {
            AuthRepositoryImpl(
                apiDataSource = get(),
                ioDispatcher = get(),
                mapper = get(),
            )
        }
        single<UserRepository> {
            UserRepositoryImpl(
                userApiService = get(),
                mapper = get(),
                ioDispatcher = get()
            )
        }

        single<GlobalSettingRepository> {
            GlobalSettingRepositoryImpl(
                apiDataSource = get(),
                ioDispatcher = get(),
                mapper = get(),
                localDataSource = get(),
            )
        }

        single<PagingDummyRepository> {
            PagingDummyRepositoryImpl(
                dataSource = get(),
                ioDispatcher = get()
            )
        }

        single<PermitRepository> {
            PermitRepositoryImpl(
                dataSource = get(),
                mapper = get(),
                ioDispatcher = get()
            )
        }

        single<AttendanceRepository> {
            AttendanceRepositoryImpl(
                datasource = get(),
                mapper = get(),
                ioDispatcher = get()
            )
        }

        single<TokenRepository>{
            TokenRepositoryImpl(get())
        }

        single<UiModeRepository>{
            UiModeRepositoryImpl(get())
        }
    }
}