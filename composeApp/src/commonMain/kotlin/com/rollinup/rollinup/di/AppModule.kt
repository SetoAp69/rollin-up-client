package com.rollinup.rollinup.di

import com.rollinup.rollinup.screen.auth.di.AuthModule
import com.rollinup.rollinup.screen.test.di.TestModule

object AppModule {
    operator fun invoke() = listOf(
        AuthModule(),
        TestModule()
    )
}