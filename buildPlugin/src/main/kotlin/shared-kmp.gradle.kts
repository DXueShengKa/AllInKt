
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
}


@OptIn(ExperimentalKotlinGradlePluginApi::class,ExperimentalWasmDsl::class)
kotlin {
    applyHierarchyTemplate(hierarchyTemplateAndroidKmp)

    val libs: VersionCatalog = versionCatalogs.named("libs")

    androidLibrary {
        compileSdk = 36
        compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
    }

    jvm()

    js {
        browser()
        binaries.library()
    }

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

