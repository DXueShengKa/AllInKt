plugins {
    id("allin.sharedKmp")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {

        commonMain {
            dependencies {
                api(libs.kotlinx.serialization.json)
                api(libs.kotlinx.serialization.protobuf)
                api(libs.kotlinx.datetime)
            }
        }
        
        nonJsCommonMain.dependencies {
            api(libs.androidx.collection)
        }

        jsMain.dependencies {
            implementation(npm("@js-joda/timezone", "2.21.1"))
        }
    }
}

android {
    namespace = "cn.allin.shared"
}
