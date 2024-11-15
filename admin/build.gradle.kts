

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {

    js(IR) {
        moduleName = "admin"
        browser {
            commonWebpackConfig {
//                cssSupport {
//                    enabled = true
//                }
                outputFileName = "admin.js"
            }
//            useEsModules()
            useCommonJs()
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
//            implementation(libs.kotlin.wrappers.mui.material)
            implementation(libs.ktorClient.js)
            implementation(projects.shared)
            implementation(projects.client.net)
            implementation(npm("antd", "5.20.1"))
            implementation(npm("@ant-design/icons", ""))
        }

    }
}
