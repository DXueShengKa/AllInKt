plugins {
    id("allin.sharedKmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared)
                implementation(libs.kotlinx.datetime)

                api(libs.ktorClient.core)
                implementation(libs.ktorClient.content.negotiation)
                implementation(libs.ktorClient.serialization.json)
            }
        }

        nonJsCommonMain.dependencies {
            implementation(libs.androidx.datastore.preferences)
        }

        jsMain.dependencies {
            api(libs.ktorClient.js)
        }
        jvmMain.dependencies {
            implementation(libs.ktorClient.java)
        }

        if (isMacOs) iosMain.dependencies {
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
