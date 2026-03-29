package com.rollinup.rollinup.security

import android.content.Context
import android.provider.Settings

fun check(context: Context) {
    val devMode = Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
    val mockLocation = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ALLOW_MOCK_LOCATION
    ) != "0"
}