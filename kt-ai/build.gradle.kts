plugins {
    id("shared-kmp")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "cn.allin.ai"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktorClient.core)
                implementation(libs.ai.koog.agents)
            }
        }
    }
}



