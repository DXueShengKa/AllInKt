
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

plugins.apply("org.jetbrains.kotlin.plugin.compose")
plugins.apply("org.jetbrains.compose")


@OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)
kotlin {


    applyHierarchyTemplate(hierarchyTemplate)

    androidTarget()

    jvm()

    wasmJs {
        browser()
        binaries.library()
    }

    if (isMacOs) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    jvmToolchain(21)


}

tasks.kotlinCompilerOptions()

androidConfigure()