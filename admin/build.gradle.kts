import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {
    
    js(KotlinJsCompilerType.IR) {
        moduleName = "admin"
        browser {
            
            commonWebpackConfig {
                outputFileName = "admin.js"
            }
            useEsModules()
        }

        binaries.executable()
    }


    sourceSets {

        jsMain.dependencies {
            implementation(project.dependencies.platform(libs.kotlin.wrappers.bom))
            implementation(libs.kotlin.wrappers.emotion)
            implementation(libs.kotlin.wrappers.react)
            implementation(libs.kotlin.wrappers.react.dom)
//            implementation(libs.kotlin.wrappers.react.router)
//            implementation(libs.kotlin.wrappers.react.router.dom)
            implementation(libs.ktorClient.js)
            implementation(projects.shared)
            implementation(projects.client.net)
            implementation(projects.client.navigationReact)
            implementation(npm("antd","5.20.1"))
        }

    }
}


