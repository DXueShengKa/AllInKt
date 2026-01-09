@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

private const val nonAndroid = "nonAndroid"

private const val jsCommon = "jsCommon"

private const val nonJsCommon = "nonJsCommon"

private const val jvmCommon = "jvmCommon"

private const val nonJvmCommon = "nonJvmCommon"

private const val native = "native"

private const val nonNative = "nonNative"



@ExperimentalKotlinGradlePluginApi
internal val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {

        group(jsCommon){
            withJs()
            withWasmJs()
        }

        group(jvmCommon){
            withJvm()
            withAndroidTarget()
        }

        group(native){
            withNative()
            group("darwin"){
                withIos()
                withMacos()
            }
        }

        group(nonAndroid){
            withJvm()
            group(jsCommon)
            group(native)
        }

        group(nonJvmCommon){
            group(jsCommon)
            group(native)
        }

        group(nonJsCommon){
            group(jvmCommon)
            group(native)
        }

        group(nonNative){
            group(jvmCommon)
            group(jsCommon)
        }

    }
}


@ExperimentalKotlinGradlePluginApi
internal val hierarchyTemplateAndroidKmp = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {

        group(jsCommon){
            withJs()
            withWasmJs()
        }

        group(jvmCommon){
            withJvm()
            //https://issuetracker.google.com/issues/442950553
            withCompilations {
                it is com.android.build.api.dsl.KotlinMultiplatformAndroidCompilation
            }
        }

        group(native){
            withNative()
            group("darwin"){
                withIos()
                withMacos()
            }
        }

        group(nonAndroid){
            withJvm()
            group(jsCommon)
            group(native)
        }

        group(nonJvmCommon){
            group(jsCommon)
            group(native)
        }

        group(nonJsCommon){
            group(jvmCommon)
            group(native)
        }

        group(nonNative){
            group(jvmCommon)
            group(jsCommon)
        }

    }
}
