package com.rollinup.rollinup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aheaditec.talsec_security.security.api.SuspiciousAppInfo
import com.aheaditec.talsec_security.security.api.Talsec
import com.aheaditec.talsec_security.security.api.TalsecConfig
import com.aheaditec.talsec_security.security.api.ThreatListener
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.rollinup.apiservice.di.AndroidDataModule
import com.rollinup.rollinup.di.AppModule
import com.rollinup.rollinup.security.SecurityEventBus
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TalsecApplication() : ComponentActivity(), ThreatListener.ThreatDetected {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initLogger()
        initKoin()
        initTalSec()
        setContent {
            AndroidApp {
                finish()
            }
        }

    }

    private fun initTalSec() {
        val config = TalsecConfig.Builder("com.rollinup.rollinup", arrayOf())
            .prod(BuildConfig.IS_PROD)
            .build()
        Talsec.start(this, config)
    }

    private fun initKoin(){
        startKoin {
            androidContext(this@TalsecApplication)
            modules(
                AndroidDataModule() + AppModule()
            )
        }
    }

    private fun initLogger(){
        L.init(LumberjackLogger)
        L.plant(ConsoleLogger())
    }


    override fun onRootDetected() {
        SecurityEventBus.rootDetected.tryEmit(Unit)
    }

    override fun onTimeSpoofingDetected() {
        SecurityEventBus.timeSpoofDetected.tryEmit(Unit)
    }

    override fun onLocationSpoofingDetected() {
        SecurityEventBus.locationSpoofDetected.tryEmit(Unit)

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

}