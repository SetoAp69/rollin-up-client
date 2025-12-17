@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.util.internal.GUtil.loadProperties
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.kotlin.serialization)
}

val envProperties = loadProperties(rootProject.file("env.properties"))

val buildGenerator by tasks.registering(Sync::class){
    from(
        resources.text.fromString(
            """
                package com.rollinup.apiservice
                object BuildConfig{
                   const val BASE_URL = "${envProperties["BASE_URL"]}"
                   const val IS_PROD = ${envProperties["IS_PROD"]}
                   const val FILE_URL = "${envProperties["FILE_URL"]}"
               }
            """.trimIndent()
        )
    ){
        rename{"BuildConfig.kt"}
        into("com/rollinup/apiservice")
    }
    into(layout.buildDirectory.dir("generated-src/kotlin/"))
}


kotlin {

    jvm()
//    wasmJs(){
//        browser()
//        nodejs()
//        d8()
//    }

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets

    group = "com.rollinup"
    androidLibrary {
        namespace = "com.rollinup.apiservice"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "rollin-up-api-serviceKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain.configure{
            kotlin.srcDir(buildGenerator.map { it.destinationDir })
        }
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.auth)
                implementation(libs.koin.core)
                implementation(libs.koin.ktor)

                //Paging
                implementation(libs.androidx.paging.common)

                //Kotlinx-datetime
                implementation(libs.kotlinx.datetime)

                //Axer
                implementation(libs.axer)

                // Add KMP dependencies here
                //Lumberjack
                implementation(libs.lumberjack.core)
                implementation(libs.implementation.lumberjack)
                implementation(libs.logger.console)

                //Datastore
                api(libs.datastore.preferences)
                api(libs.datastore)

                //common
                implementation(project(":common"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.koin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.koin.logger)
            }
        }

        nativeMain {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.koin.logger)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}

fun getProperties(path: String): Properties {
    val envFile = rootProject.file(path)

    val properties = Properties()
    properties.load(envFile.inputStream())

    return properties
}
