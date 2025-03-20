plugins {
    id("allin.sharedKmp")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktorClient.core)
                implementation(libs.openai.client)
            }
        }
    }
}


android {
    namespace = "cn.allin.ai"
}

