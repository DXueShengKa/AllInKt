plugins {
    id("compose-kmp")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        commonMain {
            dependencies {
                implementation(projects.shared)
                implementation(projects.client.components)

                api(project.dependencies.platform(libs.coil3.bom))
                api(libs.coil3.compose)
                implementation(libs.coil3.network.core)
                api(
                    libs.coil3.network.ktor3
                        .get()
                        .module
                        .toString(),
                ) {
                    exclude("io.ktor")
                }

                implementation(libs.jetbrains.components.resources)
                implementation(libs.jetbrains.components.uiToolingPPreview)
                implementation(libs.jetbrains.md3adaptive.navigation.suite)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "cn.allin.ui"
}

android {
    namespace = "cn.allin.ui"
}

dependencies {
    debugImplementation(libs.jetbrains.ui.tooling)
}
