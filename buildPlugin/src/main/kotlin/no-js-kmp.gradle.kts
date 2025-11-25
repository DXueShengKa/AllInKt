
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}


@OptIn(ExperimentalKotlinGradlePluginApi::class,ExperimentalWasmDsl::class)
kotlin {
    applyHierarchyTemplate(hierarchyTemplate)

    androidTarget()

    jvm()

    if (isMacOs) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    jvmToolchain(21)


}

tasks.kotlinCompilerOptions()

androidConfigure()