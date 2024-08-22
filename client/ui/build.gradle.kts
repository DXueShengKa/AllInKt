plugins {
    id("allin.composeKmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.material3)
            }
        }
    }
}

compose.resources {
    publicResClass = true
}


android {
    namespace = "cn.allin.ui"
}
