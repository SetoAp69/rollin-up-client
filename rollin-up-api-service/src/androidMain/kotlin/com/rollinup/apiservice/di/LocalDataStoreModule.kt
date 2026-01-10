package com.rollinup.apiservice.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.rollinup.apiservice.Constant
import com.rollinup.apiservice.data.source.datastore.AndroidLocalDataStore
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import okio.Path.Companion.toPath
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import java.io.File

object LocalDataStoreModule {
    operator fun invoke() = module {
        single<DataStore<Preferences>> {
            PreferenceDataStoreFactory.createWithPath {
                val context: Context by inject(Context::class.java)
                val file = File(
                    context
                        .filesDir
                        .resolve(Constant.LOCAL_DATA_STORE_PATH + Constant.LOCAL_DATA_STORE_NAME)
                        .absolutePath
                ).apply { parentFile?.mkdirs() }

                if (!file.exists()) {
                    file.createNewFile()
                }
                file.absolutePath.toPath()
            }
        }
        single<LocalDataStore> {
            AndroidLocalDataStore(get())
        }
    }
}