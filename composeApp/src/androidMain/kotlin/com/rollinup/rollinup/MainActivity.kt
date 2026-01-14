package com.rollinup.rollinup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.aheaditec.talsec_security.security.api.SuspiciousAppInfo
import com.aheaditec.talsec_security.security.api.Talsec
import com.aheaditec.talsec_security.security.api.TalsecConfig
import com.aheaditec.talsec_security.security.api.TalsecMode
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
        initTalSec()

        setContent {
            AndroidApp(
                authViewModel = authViewModel,
                securityViewModel = securityViewModel
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

    private fun initTalSec() {
        val config =
            TalsecConfig.Builder("com.rollinup.rollinup", arrayOf(BuildConfig.SIGNING_CERTIFICATE))
                .prod(BuildConfig.IS_PROD)
                .build()
        ThreatListener(this, deviceStateListener).registerListener(this)
        Talsec.start(this, config, TalsecMode.BACKGROUND)
    }

    private fun initViewModels() {
        val authViewmodel: AuthViewModel by viewModel()
        this.authViewModel = authViewmodel
        counterViewModel = CounterViewModel()
        val securityViewmodel: SecurityViewModel by viewModel()
        this.securityViewModel = securityViewmodel
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

    override fun onTimeSpoofingDetected() {
        securityViewModel.securityAlert(SecurityAlert.TIME_SPOOF)
    }

    override fun onLocationSpoofingDetected() {
        securityViewModel.securityAlert(SecurityAlert.LOCATION_SPOOF)
    }

    override fun onDebuggerDetected() {

    }

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

    private val deviceStateListener = object : ThreatListener.DeviceState {
        override fun onUnlockedDeviceDetected() {}

        override fun onHardwareBackedKeystoreNotAvailableDetected() {}

        override fun onDeveloperModeDetected() {}

        override fun onADBEnabledDetected() {}

        override fun onSystemVPNDetected() {}
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