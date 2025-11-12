package com.rollinup.rollinup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.rollinup.apiservice.di.AndroidDataModule
import com.rollinup.rollinup.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        L.init(LumberjackLogger)
        L.plant(ConsoleLogger())
        L.wtf { "onCreate()" }
        startKoin {
            androidContext(this@MainActivity)
            modules(
                AndroidDataModule() + AppModule()
            )
        }
        setContent {
            AndroidApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Composable
fun AndroidApp() {
    App()
}