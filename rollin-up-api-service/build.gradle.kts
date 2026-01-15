@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.util.internal.GUtil.loadProperties
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinx.kover)
}

val envProperties = loadProperties(rootProject.file("env.properties"))

val buildGenerator by tasks.registering(Sync::class) {
    from(
        resources.text.fromString(
            """
                package com.rollinup.apiservice
                object BuildConfig{
                   const val BASE_URL = "${envProperties["BASE_URL"]}"
                   val IS_PROD = ${envProperties["IS_PROD"]}
                   const val FILE_URL = "${envProperties["FILE_URL"]}"
               }
            """.trimIndent()
        )
    ) {
        rename { "BuildConfig.kt" }
        into("com/rollinup/apiservice")
    }
    into(layout.buildDirectory.dir("generated-src/kotlin/"))
}


kotlin {
    jvm()

    group = "com.rollinup"
    androidLibrary {
        namespace = "com.rollinup.apiservice"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {}

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

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

    sourceSets {
        commonMain.configure {
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
                implementation(libs.ktor.android)
            }
        }

        nativeMain {
            dependencies {
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.koin.logger)
                implementation(libs.ktor.java)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        getByName("jvmTest") {
            dependencies {
                implementation(libs.mockk)
                implementation(libs.kotlin.test)
                implementation(libs.coroutine.test)
                implementation(libs.ktor.mock)
            }
        }

        appleMain{
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }

        iosMain {
            dependencies {}
        }
    }
}

kover {
    val includedPackages = listOf(
        "*.model.*",
        "*.domain",
        "*.data.*",
        "*.source.*",
        "com.rollinup.apiservice.data.source.network.datasource.*",
    )

    val excludedPackages = listOf(
        "*.di",
        "*.utils",
        "*.model.common",
        "com.rollinup.apiservice.data.source.network.model.*",
        "com.rollinup.apiservice.data.source.datastore"
    )

    reports {
        filters {
            includes {
                packages(includedPackages)
            }
            excludes {
                packages(excludedPackages)
            }
        }
    }
}