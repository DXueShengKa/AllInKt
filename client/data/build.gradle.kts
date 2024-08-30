plugins {
    id("allin.noJsKmp")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared)
                implementation(projects.client.net)
                implementation(libs.koin.core)
                implementation(libs.koin.annotations)
            }
        }
    }
}

dependencies {
    kspNoJsAll(libs.koin.compiler)
}

android {
    namespace = "cn.allin.data"
}
