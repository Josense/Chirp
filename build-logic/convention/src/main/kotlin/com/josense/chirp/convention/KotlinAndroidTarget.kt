package com.josense.chirp.convention

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureAndroidTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidLibrary {
            namespace = this@configureAndroidTarget.pathToPackageName()
            compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
            minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
            androidResources {
                enable = true
            }
            experimentalProperties["android.experimental.kmp.enableAndroidResources"] = "true"
            experimentalProperties["android.experimental.kmp.resourcePrefix"] = this@configureAndroidTarget.pathToResourcePrefix()
            experimentalProperties["android.resourcePrefix"] = this@configureAndroidTarget.pathToResourcePrefix()

            withDeviceTest { }
            withHostTest { }

            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
}