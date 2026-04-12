import gradle.kotlin.dsl.accessors._d072288452034cc26d6adec105f80767.android
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.internal.platform.wasm.WasmPlatforms.wasmJs

plugins {
    kotlin("multiplatform")
}

plugins.apply("org.jetbrains.kotlin.plugin.compose")
plugins.apply("org.jetbrains.compose")
plugins.apply("com.android.kotlin.multiplatform.library")

@OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)
kotlin {

    val libs: VersionCatalog = versionCatalogs.named("libs")

    applyHierarchyTemplate(hierarchyTemplate)

    android {

        compileSdk =
            libs
                .findVersion("android-compileSdk")
                .get()
                .requiredVersion
                .toInt()

        minSdk =
            libs
                .findVersion("android-minSdk")
                .get()
                .requiredVersion
                .toInt()
    }

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

    sourceSets {
        val libs: VersionCatalog = versionCatalogs.named("libs")

        commonMain.dependencies {
            implementation(libs.findLibrary("jetbrains-material3").get())
        }
    }
}

