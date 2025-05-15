@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.composeCompiler)
}

val isMacOS = System.getProperty("os.name").startsWith("Mac OS")

kotlin {

    jvmToolchain(21)


    if (isMacOS) listOf(
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



        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            //iOS build failed
            implementation(libs.ktorClient.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

    }
}
