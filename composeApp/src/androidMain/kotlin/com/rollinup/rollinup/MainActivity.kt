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
import com.rollinup.CounterViewModel
import com.rollinup.apiservice.di.AndroidDataModule
import com.rollinup.rollinup.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var authViewmodel: AuthViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        L.plant(ConsoleLogger())
        L.init(LumberjackLogger)

        counterViewModel = CounterViewModel()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        startKoin {
            androidContext(this@MainActivity)
            modules(
                AndroidDataModule() + AppModule()
            )
        }

        val authViewmodel: AuthViewModel by viewModel()
        this.authViewmodel = authViewmodel

        counterViewModel.stopTimer()

        setContent {
            AndroidApp(
                authViewModel = authViewmodel
            ) {
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        counterViewModel.startTimer {
            authViewmodel.resetLoginData()
        }
    }

    override fun onResume() {
        super.onResume()
        counterViewModel.stopTimer()
    }

}

@Composable
fun AndroidApp(authViewModel: AuthViewModel, onFinish: () -> Unit) {
    App(
        authViewModel = authViewModel,
        onFinish = onFinish
    )
}