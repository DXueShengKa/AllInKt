plugins {
    id("allin.sharedKmp")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktorClient.core)
                implementation(libs.ai.aallamOpenai)
            }
        }
    }
}


android {
    namespace = "cn.allin.ai"
}

