package com.rollinup.rollinup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.aheaditec.talsec_security.security.api.SuspiciousAppInfo
import com.aheaditec.talsec_security.security.api.ThreatListener
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.rollinup.CounterViewModel
import com.rollinup.common.model.SecurityAlert
import io.github.orioneee.Axer
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity() : ComponentActivity(), ThreatListener.ThreatDetected {
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var securityViewModel: SecurityViewModel

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
        L.wtf {
            """
                ON PAUSE
                ${counterViewModel}
            """.trimIndent()
        }
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
        val securityViewModel: SecurityViewModel by viewModel()
        this.securityViewModel = securityViewModel
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

    override fun onRootDetected() {
        securityViewModel.securityAlert(SecurityAlert.ROOT)
    }

    override fun onDebuggerDetected() {}

    override fun onEmulatorDetected() {}

    override fun onTamperDetected() {}

    override fun onUntrustedInstallationSourceDetected() {}

    override fun onHookDetected() {}

    override fun onDeviceBindingDetected() {}

    override fun onObfuscationIssuesDetected() {}

    override fun onMalwareDetected(p0: List<SuspiciousAppInfo?>) {}

    override fun onScreenshotDetected() {}

    override fun onScreenRecordingDetected() {}

    override fun onMultiInstanceDetected() {}

    override fun onUnsecureWifiDetected() {}

    override fun onTimeSpoofingDetected() {}

    override fun onLocationSpoofingDetected() {
        L.w { "Location Spoofing detected" }
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