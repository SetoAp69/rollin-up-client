package com.rollinup.apiservice.di

import com.rollinup.apiservice.domain.auth.LoginJWTUseCase
import com.rollinup.apiservice.domain.auth.LoginUseCase
import com.rollinup.apiservice.domain.generalsetting.ListenGeneralSettingSSE
import com.rollinup.apiservice.domain.pagging.GetPagingDummyUseCase
import com.rollinup.apiservice.domain.user.CreateResetPasswordRequestUseCase
import com.rollinup.apiservice.domain.user.EditUserUseCase
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.domain.user.GetUserListUseCase
import com.rollinup.apiservice.domain.user.RegisterUserUseCase
import com.rollinup.apiservice.domain.user.SubmitResetOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitResetPasswordUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DomainModule {
    operator fun invoke() = module {
        authDomain()
        userDomain()
        generalSettingModule()
        pagingDummyDomain()
    }


    private fun Module.authDomain() {
        singleOf(::LoginUseCase)
        singleOf(::LoginJWTUseCase)
    }

    private fun Module.generalSettingModule() {
        singleOf(::ListenGeneralSettingSSE)
    }

    private fun Module.userDomain() {
        singleOf(::GetUserListUseCase)
        singleOf(::GetUserByIdUseCase)
        singleOf(::RegisterUserUseCase)
        singleOf(::EditUserUseCase)
        singleOf(::CreateResetPasswordRequestUseCase)
        singleOf(::SubmitResetOtpUseCase)
        singleOf(::SubmitResetPasswordUseCase)
    }

    private fun Module.pagingDummyDomain() {
        singleOf(::GetPagingDummyUseCase)
    }
}

