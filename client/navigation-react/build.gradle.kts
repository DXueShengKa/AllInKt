plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {
    
    js(IR) {
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
            api(libs.jetbrains.navigation.runtime)
            api(libs.jetbrains.lifecycle.vmSavedState)
        }

    }
}


