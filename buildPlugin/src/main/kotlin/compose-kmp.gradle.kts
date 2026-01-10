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

    sourceSets {
        val libs: VersionCatalog = versionCatalogs.named("libs")

        commonMain.dependencies {
            implementation(libs.findLibrary("jetbrains-material3").get())
        }
    }
}

tasks.kotlinCompilerOptions()

android {
    val libs: VersionCatalog = versionCatalogs.named("libs")

    compileSdk =
        libs
            .findVersion("android-compileSdk")
            .get()
            .requiredVersion
            .toInt()

    defaultConfig {
        minSdk =
            libs
                .findVersion("android-minSdk")
                .get()
                .requiredVersion
                .toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
