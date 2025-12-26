package com.rollinup.rollinup

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.rollinup.apiservice.di.AndroidDataModule
import com.rollinup.rollinup.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        L.init(LumberjackLogger)
        L.plant(ConsoleLogger())
        L.wtf { "onCreate()" }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        startKoin {
            androidContext(this@MainActivity)
            modules(
                AndroidDataModule() + AppModule()
            )
        }

        setContent {
            AndroidApp {
                finish()
            }
        }
    }
}

@Composable
fun AndroidApp(onFinish: () -> Unit) {
    App(onFinish)
}