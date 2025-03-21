@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.ksp)
}

val isTest: String by project

kotlin {

    androidTarget()

    jvm("desktop")

    val osName = System.getProperty("os.name")
    if (osName.startsWith("Mac OS")) listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        commonMain.dependencies {
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.jetbrains.adaptive.navigation)
            implementation(libs.koin.kmp.compose.viewmodel)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)
            implementation(projects.client.ui)
            implementation(projects.client.data)
            implementation(projects.client.components)
            implementation(projects.kspAnnotation)

            implementation(libs.lazytable)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
//            implementation(libs.koin.test)
        }

        desktopMain.dependencies {
            implementation(libs.slf4j.simple)
            implementation(compose.desktop.currentOs)
            implementation(libs.jspecify)
        }
    }
}

android {
    namespace = "cn.allin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
        resources.srcDirs("src/commonMain/resources")
    }

    defaultConfig {
        applicationId = "cn.allin"
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
        release {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

}

compose.desktop {

    application {
        mainClass = "cn.allin.MainKt"

        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.AppImage)
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "cn.allin"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            version.set(libs.versions.proguard.get())
            configurationFiles.from("jvmProguard.pro")
        }
    }
}

dependencies {
    ksp(projects.allKsp)
}
