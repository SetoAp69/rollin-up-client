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
    private lateinit var securityViewModel: SecurityViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        initLogger()
        initKoin()
        initViewModels()

        counterViewModel.stopTimer()

        setContent {
            AndroidApp(
                securityViewModel = securityViewModel,
                authViewModel = authViewmodel
            ) {
                finish()
            }
        }
    }

    private fun initLogger() {
        L.init(LumberjackLogger)
        L.plant(ConsoleLogger())
    }

    private fun initViewModels() {
        val authViewmodel: AuthViewModel by viewModel()
        this.authViewmodel = authViewmodel
        counterViewModel = CounterViewModel()
        val securityViewmodel: SecurityViewModel by viewModel()
        this.securityViewModel= securityViewmodel
    }


    private fun initKoin() {
        startKoin {
            androidContext(this@MainActivity)
            modules(
                AndroidDataModule() + AppModule()
            )
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
fun AndroidApp(
    securityViewModel: SecurityViewModel,
    authViewModel: AuthViewModel,
    onFinish: () -> Unit,
) {
    App(
        securityViewModel = securityViewModel,
        authViewModel = authViewModel,
        onFinish = onFinish
    )
}