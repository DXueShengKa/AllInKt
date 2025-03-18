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
                sourceMaps = false
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

        compilerOptions {
            target = "es2015"
        }
    }


    sourceSets {

        jsMain.dependencies {
            implementation(project.dependencies.platform(libs.kotlin.wrappers.bom))
            implementation(libs.kotlin.wrappers.emotion)
            implementation(libs.kotlin.wrappers.react)
            implementation(libs.kotlin.wrappers.react.dom)
            implementation(libs.kotlin.wrappers.react.router)
            implementation(libs.kotlin.wrappers.mui.material)
//            implementation(libs.kotlin.wrappers.mui.icons)
            implementation(libs.kotlin.wrappers.muix.treeView)
            implementation(libs.kotlin.wrappers.tanstack.table)
            implementation(libs.kotlin.wrappers.tanstack.query)
            implementation(npm("@mui/x-date-pickers",""))

            implementation(npm("@mui/material","^6"))
//            implementation(npm("@mui/icons-material","^6"))
            implementation(npm("react-router","^7"))

            implementation(npm("@toolpad/core",libs.versions.toolpad.core.get()))
            implementation(npm("dayjs",libs.versions.dayjs.get()))
            implementation(projects.shared)
            implementation(projects.client.net)
        }
    }
}
