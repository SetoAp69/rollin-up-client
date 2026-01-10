package com.rollinup.rollinup

import android.app.Application
import com.rollinup.apiservice.di.AndroidDataModule
import com.rollinup.rollinup.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin(){
        startKoin {
            androidContext(this@MainApplication)
            modules(AndroidDataModule() + AppModule())
        }
    }
}