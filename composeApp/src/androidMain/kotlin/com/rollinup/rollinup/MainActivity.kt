package com.rollinup.rollinup

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
import io.github.orioneee.Axer
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity() : ComponentActivity() {
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initLogger()
        initViewModels()

        setContent {
            AndroidApp(
                authViewModel = authViewModel
            ) {
                finishAndRemoveTask()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        counterViewModel.startTimer {
            authViewModel.resetLoginData()
        }
    }

    override fun onResume() {
        super.onResume()
        counterViewModel.stopTimer()
    }


    private fun initViewModels() {
        val authViewmodel: AuthViewModel by viewModel()
        this.authViewModel = authViewmodel
        counterViewModel = CounterViewModel()
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
}


@Composable
fun AndroidApp(
    authViewModel: AuthViewModel,
    onFinish: () -> Unit,
) {
    App(
        authViewModel = authViewModel,
        onFinish = onFinish
    )
}