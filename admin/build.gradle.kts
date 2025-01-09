import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {

    js {
        moduleName = "admin"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
                outputFileName = "admin.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
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
            implementation(projects.shared)
            implementation(projects.client.net)
            implementation(npm("antd", "5.23.0"))
            implementation(npm("@ant-design/icons", ""))
        }

    }
}
