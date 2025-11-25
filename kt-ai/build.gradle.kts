plugins {
    id("shared-kmp")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktorClient.core)
                implementation(libs.ai.koog.agents)
            }
        }
    }
}


android {
    namespace = "cn.allin.ai"
}

