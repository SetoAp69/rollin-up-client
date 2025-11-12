package com.rollinup.rollinup.screen.test.di

import com.rollinup.rollinup.screen.test.ui.viewmodel.PagingDemoViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object TestModule {
    operator fun invoke() = module {
        viewModelOf(::PagingDemoViewModel)
    }
}