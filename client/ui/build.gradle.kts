plugins {
    id("allin.composeKmp")
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
                api(libs.coil3.network.ktor3.get().module.toString()){
                    exclude("io.ktor")
                }

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.material3)
                implementation(compose.material3AdaptiveNavigationSuite)
            }
        }
    }
}

compose.resources {
    publicResClass = true
}


android {
    namespace = "cn.allin.ui"

    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
