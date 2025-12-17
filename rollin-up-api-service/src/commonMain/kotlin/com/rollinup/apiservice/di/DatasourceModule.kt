package com.rollinup.apiservice.di

import com.rollinup.apiservice.Constant
import com.rollinup.apiservice.data.source.network.apiservice.AttendanceApiService
import com.rollinup.apiservice.data.source.network.apiservice.AuthApiService
import com.rollinup.apiservice.data.source.network.apiservice.GlobalSettingApiService
import com.rollinup.apiservice.data.source.network.apiservice.PagingDummyApi
import com.rollinup.apiservice.data.source.network.apiservice.PermitApiService
import com.rollinup.apiservice.data.source.network.apiservice.UserApiService
import com.rollinup.apiservice.data.source.network.datasource.attendance.AttendanceApiDataSource
import com.rollinup.apiservice.data.source.network.datasource.auth.AuthApiDataSource
import com.rollinup.apiservice.data.source.network.datasource.globalsetting.GlobalSettingApiDataSource
import com.rollinup.apiservice.data.source.network.datasource.paging.PagingDummyDataSource
import com.rollinup.apiservice.data.source.network.datasource.permit.PermitApiDataSource
import com.rollinup.apiservice.data.source.network.datasource.user.UserApiDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DatasourceModule {
    operator fun invoke() = module {
        single<AuthApiService> { AuthApiDataSource(get(named(Constant.HTTP_CLIENT))) }
        single<UserApiService> { UserApiDataSource(get(named(Constant.HTTP_CLIENT))) }
        single<GlobalSettingApiService> {
            GlobalSettingApiDataSource(
                httpClient = get(named(Constant.HTTP_CLIENT)),
                sseClient = get(named(Constant.SSE_CLIENT))
            )
        }
        single<PagingDummyApi> {
            PagingDummyDataSource(
                httpClient = get(named(Constant.HTTP_CLIENT))
            )
        }
        single<AttendanceApiService> {
            AttendanceApiDataSource(
                httpClient = get(named(Constant.HTTP_CLIENT))
            )
        }
        single<PermitApiService> {
            PermitApiDataSource(
                httpClient = get(named(Constant.HTTP_CLIENT))
            )
        }
    }
}