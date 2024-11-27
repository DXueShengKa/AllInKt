plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {
    
    js {
        browser {
            useEsModules()
        }
        binaries.library()
    }


    sourceSets {

        jsMain.dependencies {
            implementation(project.dependencies.platform(libs.kotlin.wrappers.bom))
            implementation(libs.kotlin.wrappers.react)
            implementation(libs.kotlin.wrappers.react.dom)
            implementation(libs.jetbrains.navigation.runtime)
            implementation(libs.jetbrains.lifecycle.vmSavedState)
        }

    }
}


