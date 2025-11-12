package com.rollinup.rollinup.screen.auth.di

import com.rollinup.rollinup.AuthViewModel
import com.rollinup.rollinup.GeneralSettingViewModel
import com.rollinup.rollinup.screen.auth.model.login.TestClass
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.viewmodel.LoginViewModel
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.viewmodel.ResetPasswordViewModel
import com.rollinup.rollinup.screen.splashscreen.viewmodel.SplashScreenViewmodel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AuthModule {
    operator fun invoke() = module {
        singleOf(::TestClass)
        viewModelOf(::LoginViewModel)
        viewModelOf(::ResetPasswordViewModel)
        viewModelOf(::SplashScreenViewmodel)
        viewModelOf(::AuthViewModel)
        viewModelOf(::GeneralSettingViewModel)
    }
}