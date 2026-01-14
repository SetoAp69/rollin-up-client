@file:Suppress("UnstableApiUsage")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

val envProperties = Properties()
val envPropertiesFile = rootProject.file("env.properties")
if (envPropertiesFile.exists()) {
    envPropertiesFile.inputStream().use { envProperties.load(it) }
}

val secret = Properties()
val secretFile = rootProject.file("secret.properties")
if (secretFile.exists()) {
    secretFile.inputStream().use { secret.load(it) }
}

val buildConfigGenerator by tasks.registering(Sync::class) {
    from(
        resources.text.fromString(
            """
                package com.rollinup.rollinup
                object BuildConfig{
                   const val MAP_URL = "${envProperties["MAP_URL"]}"
                   val IS_PROD = ${envProperties["IS_PROD"]}
                   const val SIGNING_CERTIFICATE = "${secret["SIGNING_CERTIFICATE"]}"
               }
            """.trimIndent()
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

    jvm {
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
//            implementation(libs.kotlinx.dataframe.excel)

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

            //firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosTest.dependencies {
            implementation(libs.permission.test)
        }
        androidUnitTest.dependencies {
            implementation(libs.permission.test)
            implementation(libs.mockk)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.java)
            implementation(libs.kcef)

            //Dataframe
            implementation(libs.kotlinx.dataframe)

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
    namespace = libs.versions.name.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    androidResources {
        generateLocaleConfig = true
        localeFilters.add("en")
        localeFilters.add("id")
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("/keystore/release-keystore.jks")
            storePassword = secret["KEYSTORE_PASSWORD"]?.toString()
            keyAlias = secret["KEYSTORE_ALIAS"]?.toString()
            keyPassword = secret["KEY_PASSWORD"]?.toString()
        }
    }

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = libs.versions.version.get()
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        resources.pickFirsts.add("/META-INF/kotlin-jupyter-libraries/libraries.json")
        resources.excludes.add("/META-INF/thirdparty-LICENSE")
        resources.pickFirsts.add("arrow-git.properties")
        resources.pickFirsts.add("META-INF/DEPENDENCIES")
    }

    buildTypes {
        getByName("release") {
            manifestPlaceholders += mapOf()
            isMinifyEnabled = true
            versionNameSuffix = "release"
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "true"
            manifestPlaceholders["version"] = defaultConfig.versionName + "-" + versionNameSuffix
            manifestPlaceholders["appName"] = "@string/app_name"
            proguardFile(rootProject.file("androidProguard.pro"))
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            manifestPlaceholders["version"] = defaultConfig.versionName + "-" + versionNameSuffix
            manifestPlaceholders["appName"] = "@string/app_name"
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "false"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    applicationVariants.all {
        val sdf = SimpleDateFormat("ddMMyyyy_HHmmss")
        val currentDateTime = sdf.format(Date())
        val appName = libs.versions.name.get()
        val version = libs.versions.version.get()

        outputs
            .map {
                it as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            }
            .forEach { output ->
                val variant = this.buildType.name
                val fileExtension = output.outputFile.name.substringAfterLast(".")
                output.outputFileName =
                    "$appName-${variant}_v$version-$currentDateTime.$fileExtension"
            }

        outputs
            .forEach { _ ->
                val variant = buildType.name
                val bundleTaskName = "bundle${variant.replaceFirstChar { it.uppercase() }}"
                tasks.named(bundleTaskName).configure {
                    doLast {
                        val newFileName = "$appName-${variant}_v$version-$currentDateTime.aab"
                        val bundleOutputDir = file("build/outputs/bundle/$variant")
                        val bundle =
                            bundleOutputDir.listFiles()?.firstOrNull { it.name.endsWith(".aab") }

                        bundle?.let {
                            val newFile = File(bundleOutputDir, newFileName)
                            newFile.createNewFile()
                            bundle.renameTo(newFile)
                        } ?: run {
                            println("Bundle file not found!")
                        }
                    }
                }
            }
        applicationVariants.forEach { variant->
            if(variant.buildType.isMinifyEnabled){
                variant.assembleProvider.get().doLast {
                    val mappingFiles = variant.mappingFileProvider.get().files
                    mappingFiles.forEach { file->
                        if(file.exists()){
                            val mapName = "$appName-${variant.name}_v$version-$currentDateTime-mapping.${file.extension}"
                            val mapFile = File(file.parent, mapName)

                            file.copyTo(mapFile)
                        }
                    }
                }
            }
        }
    }
}

dependencies {
    //Test
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.coroutine.test)

    //Tooling
    debugImplementation(compose.uiTooling)

    //Firebase
    releaseImplementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)

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
        version = libs.versions.version.get()
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "com.rollinup.rollinup"
            packageVersion = libs.versions.version.get()
            includeAllModules = true
            modules("jdk.jcef")

            windows{
                iconFile.set(rootProject.file("assets/icon/rollin-up-logo-512.ico"))
                exePackageVersion = libs.versions.version.get()
                msiPackageVersion = libs.versions.version.get()
                shortcut = true
                dirChooser = true
            }
            linux{
                iconFile.set(rootProject.file("assets/icon/rollin-up-logo-512.png"))
                debMaintainer = libs.versions.vendor.get()
                shortcut = true
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from(rootProject.file("desktopProguard.pro"))
        }
    }
}

kover {
    val includePackage = listOf(
        "*.model.*",
        "*.viewmodel",
    )

    val excludedPackage = listOf(
        "*.di.*",
        "*.test.*",
        "*.view",
        "*.utils",
        "*.view.*",
        ""
    )

    reports {
        filters {
            includes {
                packages(includePackage)
            }
            excludes {
                packages(excludedPackage)
            }
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
