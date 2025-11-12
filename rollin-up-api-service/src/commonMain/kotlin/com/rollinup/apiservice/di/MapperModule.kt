package com.rollinup.apiservice.di

import com.rollinup.apiservice.data.mapper.GeneralSettingMapper
import com.rollinup.apiservice.data.mapper.LoginMapper
import com.rollinup.apiservice.data.mapper.UserMapper
import org.koin.dsl.module

object MapperModule {
    operator fun invoke() = module {
        single<UserMapper> { UserMapper() }
        single<LoginMapper> { LoginMapper() }
        single<GeneralSettingMapper> { GeneralSettingMapper() }
    }
}