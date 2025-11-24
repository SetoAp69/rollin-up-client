package com.rollinup.rollinup

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.rollinup.apiservice.di.JVMDataModule
import com.rollinup.rollinup.di.AppModule
import org.koin.core.context.startKoin

fun main() = application {
    L.init(LumberjackLogger)
    L.plant(ConsoleLogger())

    startKoin {
        modules(
            JVMDataModule() + AppModule()
        )
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "rollin-up",
    ) {
        App()
    }
}