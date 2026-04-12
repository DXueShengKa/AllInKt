@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library.kmp)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.ksp)
}

val isTest: String by project

val isMacOS = System.getProperty("os.name").startsWith("Mac OS")

kotlin {

    jvmToolchain(21)

    android {
        namespace = "cn.allin.composeApp"

        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()

        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    if (isMacOS) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        commonMain.dependencies {
            implementation(libs.jetbrains.material3)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.md3adaptive.navigation)
            implementation(libs.jetbrains.lifecycle.compose)
            implementation(libs.jetbrains.lifecycle.navigation3)
            implementation(libs.jetbrains.savedstate.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jetbrains.components.resources)
            implementation(projects.shared)
            implementation(projects.client.ui)
            implementation(projects.client.net)
            implementation(projects.client.data)
            implementation(projects.client.components)
            implementation(projects.ksp.annotation)

            implementation(libs.lazytable)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
//            implementation(libs.koin.test)
        }

        jvmMain.dependencies {
            implementation(libs.slf4j.simple)
            implementation(compose.desktop.currentOs) {
                exclude("org.jetbrains.compose.material", "material-desktop")
            }
            implementation(libs.jspecify)
        }
    }
}

compose.resources {
    packageOfResClass = "cn.allin.res"
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

// plugins.withType<NodeJsPlugin>{
//    the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec>()
//        .version = "22.18.0"
// }

fun DependencyHandler.kspAll(dependencyNotation: Any) {
    add("kspAndroid", dependencyNotation)
    add("kspJvm", dependencyNotation)
    add("kspWasmJs", dependencyNotation)
    if (isMacOS) {
        add("kspIosSimulatorArm64", dependencyNotation)
        add("kspIosArm64", dependencyNotation)
        add("kspIosX64", dependencyNotation)
    }
}

dependencies {
    kspAll(projects.ksp.composeApp)
}
