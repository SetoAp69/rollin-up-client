package com.rollinup.rollinup

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.multiplatform.webview.util.addTempDirectoryRemovalHook
import com.rollinup.apiservice.di.JVMDataModule
import com.rollinup.rollinup.di.AppModule
import io.github.orioneee.Axer
import io.github.orioneee.AxerTrayWindow
import org.koin.core.context.startKoin

fun main() = application {
    initLogger()
    addTempDirectoryRemovalHook()
    startKoin {
        modules(
            JVMDataModule() + AppModule()
        )
    }
    Window(
        state = rememberWindowState(placement = WindowPlacement.Maximized),
        onCloseRequest = ::exitApplication,
        title = "rollin-up",
    ) {
        App(onFinish = ::exitApplication)
    }
    if (!BuildConfig.IS_PROD) {
        AxerTrayWindow(initialValue = false)
    }
}

private fun initLogger() {
    val enableLogging = !BuildConfig.IS_PROD

    L.init(LumberjackLogger)
    L.plant(ConsoleLogger())

    Axer.configure {
        enableRequestMonitor = enableLogging
        enableExceptionMonitor = enableLogging
        enableLogMonitor = enableLogging
        enableDatabaseMonitor = enableLogging
    }
}

