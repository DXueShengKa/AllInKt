import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {

    js {
        outputModuleName = "admin"
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

            testTask {

                useKarma { //运行测试用的，安装了哪个浏览器就开哪个

//                    useChromium()
                    useFirefox()
                }
            }
        }

        useCommonJs()

        binaries.executable()

        compilerOptions {
            target = "es2015"
        }
    }


    sourceSets {

        jsMain.dependencies {
            implementation(project.dependencies.platform(libs.kotlin.wrappers.bom))
            implementation(libs.kotlin.wrappers.emotion.react)
            implementation(libs.kotlin.wrappers.react)
            implementation(libs.kotlin.wrappers.react.dom)
            implementation(libs.kotlin.wrappers.react.router)
            implementation(libs.kotlin.wrappers.mui.material)
            implementation(libs.kotlin.wrappers.muix.treeView)
            implementation(libs.kotlin.wrappers.tanstack.table)
            implementation(npm("@mui/x-date-pickers",""))

            implementation(npm("@mui/material",libs.versions.mui.material.get()))
            //用mui kt封装库的，打包时不会被压缩，生成产物过大, 需要图标再手动声明
            implementation(npm("@mui/icons-material",libs.versions.mui.material.get()))
            implementation(npm("react-router","^7"))

            implementation(npm("@toolpad/core",libs.versions.mui.toolpad.get()))
            implementation(npm("dayjs",libs.versions.dayjs.get()))
            implementation(npm("@emotion/styled",libs.versions.emotionStyled.get()))
            implementation(npm("mui-file-input",libs.versions.mui.fileInput.get()))
            implementation(projects.shared)
            implementation(projects.client.net)
            implementation(projects.client.data)
            implementation(libs.koin.core)
        }

        jsTest.dependencies {
            implementation(kotlin("test-js"))
        }
    }
}
