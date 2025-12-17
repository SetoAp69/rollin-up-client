import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.composeHotReload)
}

val envProperties = Properties()
val envPropertiesFile = rootProject.file("env.properties")
if (envPropertiesFile.exists()) {
    envPropertiesFile.inputStream().use { envProperties.load(it) }
}

val buildConfigGenerator by tasks.registering(Sync::class) {
    from(
        resources.text.fromString(
            """
                package com.rollinup.rollinup
                object BuildConfig{
                   const val MAP_URL = "${envProperties["MAP_URL"]}"
                   const val IS_PROD = ${envProperties["IS_PROD"]}
                   const val TEMP_PASSWORD = ${envProperties["TEMP_PASSWORD"]}
               }
            """.trimMargin()
        )
    ) {
        rename { "BuildConfig.kt" }
        into("com/rollinup/rollinup")
    }
    into(layout.buildDirectory.dir("generated-src/kotlin/"))
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        languageSettings.optIn("kotlin.time.ExperimentalTime")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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

    jvm() {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//        binaries.executable()
//    }

    sourceSets {
        commonMain.configure {
            kotlin.srcDir(buildConfigGenerator.map { it.destinationDir })
        }

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

            //talsec
            implementation(libs.talsec.security)

            //Dataframe
            implementation(libs.kotlinx.dataframe)
            implementation(libs.kotlinx.dataframe.excel)

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

            //Webview
            implementation(libs.compose.webview.multiplatform)

            //Kotlinx-datetime
            implementation(libs.kotlinx.datetime)

            //wheel picker
            implementation(libs.datetime.wheel.picker)

            //UI Backhandler
            implementation(libs.ui.backhandler)

            //Lumberjack
            implementation(libs.lumberjack.core)
            implementation(libs.implementation.lumberjack)
            implementation(libs.logger.console)

            //Api service
            implementation(project(":rollin-up-api-service"))
            implementation(project(":common"))

            //multiplatform settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.datastore)

            //Paging
            implementation(libs.androidx.paging.compose)
            implementation(libs.androidx.paging.common)

            //Datastore
            api(libs.datastore.preferences)
            api(libs.datastore)

            //Calendar
            implementation(libs.kizitonwose.calendar)

            //Compass
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geolocation)

            //inspektor
            implementation(libs.axer)

            //Compottie
            implementation(libs.compottie)
            implementation(libs.compottie.dot)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosTest.dependencies {
            implementation(libs.permission.test)
        }
        androidUnitTest.dependencies {
            implementation(libs.permission.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
//            implementation(libs.skiko.awt.runtime.windows.x64)
//            implementation(compose.desktop.windows_arm64)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.java)
            implementation(libs.kcef)

            //Dataframe
            implementation(libs.kotlinx.dataframe)
            implementation(libs.kotlinx.dataframe.excel)

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

        val mobileMain by creating {
            dependsOn(commonMain.get())
            androidMain.get().dependsOn(this)
            iosMain.get().dependsOn(this)
            iosArm64Main.get().dependsOn(this)
            iosSimulatorArm64Main.get().dependsOn(this)
            dependencies {

            }
        }

        val desktopMain by creating {
            dependsOn(commonMain.get())
            jvmMain.get().dependsOn(this)
            dependencies {
                implementation(libs.compass.geocoder.web.googlemaps)
            }
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

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jogamp.org/deployment/maven") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://europe-west3-maven.pkg.dev/talsec-artifact-repository/freerasp") }
}

compose.desktop {
    application {
        mainClass = "com.rollinup.rollinup.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.rollinup.rollinup"
            packageVersion = "1.0.0"
            includeAllModules = true
            modules("jdk.jcef")
        }

        buildTypes.release.proguard {
            configurationFiles.from(rootProject.file("desktopProguard.pro"))
        }
    }
}

afterEvaluate {
    tasks.withType<JavaExec> {
        jvmArgs("-Dsun.java2d.uiScale=1.25")
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}
