plugins {
    id("compose-kmp")
}



kotlin {

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            api(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.jsoup)
            api(libs.kotlinx.coroutines.swing)
        }

        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.androidx.paging.common)

            implementation(projects.shared)
            implementation(libs.kotlinx.datetime)
            implementation(libs.jetbrains.ui.util)
        }
    }
}

android {
    namespace = "com.compose.components"
}
