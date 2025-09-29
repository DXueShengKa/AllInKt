plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    wasmJs {
        browser()
        binaries.library()
    }

    val osName = System.getProperty("os.name")
    if (osName == "Mac OS X"){
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }


    sourceSets {
        commonMain.dependencies {
        }
    }
}
