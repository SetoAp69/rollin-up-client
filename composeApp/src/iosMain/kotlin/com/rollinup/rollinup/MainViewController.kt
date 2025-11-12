package com.rollinup.rollinup

import androidx.compose.ui.window.ComposeUIViewController
import com.rollinup.apiservice.di.IOSDataModule
import com.rollinup.rollinup.di.AppModule
import org.koin.core.context.startKoin

fun MainViewController() = {
    startKoin {
        modules(
            IOSDataModule() + AppModule()
        )
    }
    ComposeUIViewController { App() }
}