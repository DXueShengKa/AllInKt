plugins {
    id("allin.sharedKmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.protobuf)
                implementation(libs.kotlinx.datetime)

                api(libs.ktorClient.core)
                implementation(libs.ktorClient.content.negotiation)
                implementation(libs.ktorClient.serialization.json)
            }
        }
        jvmMain.dependencies {
            implementation(libs.ktorClient.java)
        }
        iosMain.dependencies {
            implementation(libs.ktorClient.darwin)
        }
        androidMain.dependencies {
            implementation(libs.ktorClient.okhttp)
        }
    }
}



android {
    namespace = "cn.allin.net"
}
