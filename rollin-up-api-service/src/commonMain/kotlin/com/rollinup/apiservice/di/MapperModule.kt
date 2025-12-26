package com.rollinup.apiservice.di

import com.rollinup.apiservice.data.mapper.AttendanceMapper
import com.rollinup.apiservice.data.mapper.GlobalSettingMapper
import com.rollinup.apiservice.data.mapper.AuthMapper
import com.rollinup.apiservice.data.mapper.PermitMapper
import com.rollinup.apiservice.data.mapper.UserMapper
import org.koin.dsl.module

object MapperModule {
    operator fun invoke() = module {
        single<UserMapper> { UserMapper() }
        single<AuthMapper> { AuthMapper() }
        single<GlobalSettingMapper> { GlobalSettingMapper() }
        single<AttendanceMapper> { AttendanceMapper() }
        single<PermitMapper> { PermitMapper() }
    }
}