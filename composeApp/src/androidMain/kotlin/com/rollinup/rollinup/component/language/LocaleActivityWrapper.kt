package com.rollinup.rollinup.component.language

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Resources

class LocaleActivityWrapper(
    val baseActivity: Context,
    private val localizedContext: Context,
) : ContextWrapper(baseActivity) {
    override fun getResources(): Resources? {
        return localizedContext.resources
    }

    override fun getAssets(): AssetManager? {
        return localizedContext.assets
    }

    override fun getTheme(): Resources.Theme? {
        return localizedContext.theme
    }
}