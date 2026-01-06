plugins {
    id("shared-kmp")
    alias(libs.plugins.ksp)
}

kotlin {
    androidLibrary {
        namespace = "cn.allin.data"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared)
                implementation(projects.client.net)
                implementation(libs.koin.core)
                implementation(libs.koin.annotations)
                api(libs.kotlinx.io.core)
                api(libs.kotlinx.io.bytestring)
            }
        }
    }
}

ksp {
    arg("KOIN_DEFAULT_MODULE", "false")
}

dependencies {
    kspAll(libs.koin.compiler)
}
