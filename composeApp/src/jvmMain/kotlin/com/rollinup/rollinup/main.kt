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
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.di.AppModule
import io.github.orioneee.AxerTrayWindow
import kotlinx.datetime.TimeZone
import org.koin.core.context.startKoin

fun main() = application {
    L.init(LumberjackLogger)
    L.plant(ConsoleLogger())
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
    AxerTrayWindow()
}


fun mainx() {
    val dt = "2025-12-11T15:04:22.440574Z".parseToLocalDateTime(TimeZone.currentSystemDefault())
    println(dt)
}