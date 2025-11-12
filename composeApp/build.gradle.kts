import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.composeHotReload)
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//        binaries.executable()
//    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //Ktor
            implementation(libs.ktor.android)

            //Lumberjack
            implementation(libs.lumberjack.android)
            implementation(libs.core.splashscreen)

            //compass
            implementation(libs.compass.geocoder.mobile)
            implementation(libs.compass.geolocation.mobile)

            //Camera
            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)

            //moko permission
            implementation(libs.permission.camera)
            implementation(libs.permission.gallery)
            implementation(libs.permission.location)
            api(libs.permission.compose)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.compose.navigation)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            //UI Backhandler
            implementation(libs.ui.backhandler)

            //Lumberjack
            implementation(libs.lumberjack.core)
            implementation(libs.implementation.lumberjack)
            implementation(libs.logger.console)

            //Api service
            implementation(project(":rollin-up-api-service"))

            //multiplatform settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.datastore)

            //Paging
//            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.androidx.paging.compose)
            implementation(libs.androidx.paging.common)

            //Datastore
            api(libs.datastore.preferences)
            api(libs.datastore)

            //Compass
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geolocation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

        }
        iosTest.dependencies{
            implementation(libs.permission.test)
        }
        androidUnitTest.dependencies{
            implementation(libs.permission.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.java)
        }
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
            implementation(libs.compass.geocoder.mobile)
            implementation(libs.compass.geolocation.mobile)

            //moko permission
            implementation(libs.permission.camera)
            implementation(libs.permission.gallery)
            implementation(libs.permission.location)
            api(libs.permission.compose)
        }


    }
}

android {
    namespace = "com.rollinup.rollinup"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.rollinup.rollinup"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    coreLibraryDesugaring(libs.desugar.jdk)
}

compose.desktop {
    application {
        mainClass = "com.rollinup.rollinup.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.rollinup.rollinup"
            packageVersion = "1.0.0"
        }
    }
}
