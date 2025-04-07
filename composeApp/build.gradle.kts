@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

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

//    jvm("desktop")

    val osName = System.getProperty("os.name")
    if (osName.startsWith("Mac OS")) listOf(
        iosX64(),
        iosArm64(),
//        iosSimulatorArm64()
    ).forEach { iosTarget ->

        val basePath = "$rootDir/composeApp/src/iosMain/framework"

        //根据设备类型指定库文件所在文件名
        val abiPath = if (iosTarget.name == "iosArm64") "ios-arm64"
        else "ios-arm64_x86_64-simulator"

        iosTarget.compilations.named("main") {
            //https://doc.zh-jieli.com/Apps/iOS/ota/zh-cn/master/Framework/framework.html
            val jlLib by cinterops.creating {
                definitionFile = file("src/iosMain/cinterop/jlLib.def")
                //-F 指定库目录所在位置（绝对路径）
                //-framework 指定这个库的名字
                compilerOpts(
                    "-F", "$basePath/JL_OTALib.xcframework/$abiPath",
                    "-framework", "JL_OTALib",

                    "-F", "$basePath/JL_AdvParse.xcframework/$abiPath",
                    "-framework", "JL_AdvParse",

                    "-F", "$basePath/JL_HashPair.xcframework/$abiPath",
                    "-framework", "JL_HashPair",

                    "-F", "$basePath/JL_BLEKit.xcframework/$abiPath",
                    "-framework", "JL_BLEKit",

                    "-F", "$basePath/JLLogHelper.xcframework/$abiPath",
                    "-framework", "JLLogHelper",
                )
            }
        }


        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            //https://doc.zh-jieli.com/Apps/Android/ota/zh-cn/master/framework/framework.html
            implementation(fileTree(mapOf("dir" to "src/androidMain/libs", "include" to listOf("*.jar", "*.aar"))))
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

            implementation(libs.bluefalcon)
        }

        iosMain.dependencies {
            implementation(libs.ktorClient.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
//            implementation(libs.koin.test)
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


dependencies {
    ksp(projects.allKsp)
}
