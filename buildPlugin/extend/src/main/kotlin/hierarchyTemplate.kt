@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

@ExperimentalKotlinGradlePluginApi
val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {
        withCompilations { true }

        groupNonAndroid()
        groupJsCommon()
        groupNonJsCommon()
        groupJvmCommon()
        groupNonJvmCommon()
        groupNative()
        groupNonNative()
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.main(
    name: String,
    action: Action<KotlinSourceSet>
): KotlinSourceSet =
    named("${name}Main", action).get()


private const val nonAndroid = "nonAndroid"

fun NamedDomainObjectContainer<KotlinSourceSet>.nonAndroidMain(action: Action<KotlinSourceSet>) =
    main(nonAndroid, action)

private fun KotlinHierarchyBuilder.groupNonAndroid() {
    group(nonAndroid) {
        withJvm()
        groupJsCommon()
        groupNative()
    }
}


private const val jsCommon = "jsCommon"

fun NamedDomainObjectContainer<KotlinSourceSet>.jsCommonMain(action: Action<KotlinSourceSet>) =
    main(jsCommon, action)

private fun KotlinHierarchyBuilder.groupJsCommon() {
    group(jsCommon) {
        withJs()
        withWasmJs()
    }
}

private const val nonJsCommon = "nonJsCommon"

fun NamedDomainObjectContainer<KotlinSourceSet>.nonJsCommonMain(action: Action<KotlinSourceSet>) =
    main(nonJsCommon, action)

private fun KotlinHierarchyBuilder.groupNonJsCommon() {
    group(nonJsCommon) {
        groupJvmCommon()
        groupNative()
    }
}


private const val jvmCommon = "jvmCommon"

fun NamedDomainObjectContainer<KotlinSourceSet>.jvmCommonMain(action: Action<KotlinSourceSet>) =
    main(jvmCommon, action)

private fun KotlinHierarchyBuilder.groupJvmCommon() {
    group(jvmCommon) {
        withAndroidTarget()
        withJvm()
    }
}

private const val nonJvmCommon = "nonJvmCommon"

fun NamedDomainObjectContainer<KotlinSourceSet>.nonJvmCommonMain(action: Action<KotlinSourceSet>) =
    main(nonJvmCommon, action)

private fun KotlinHierarchyBuilder.groupNonJvmCommon() {
    group(nonJvmCommon) {
        groupJsCommon()
        groupNative()
    }
}

private const val native = "native"
fun NamedDomainObjectContainer<KotlinSourceSet>.nativeMain(action: Action<KotlinSourceSet>) =
    main(native, action)
private fun KotlinHierarchyBuilder.groupNative() {
    group(native) {
        withNative()

        //当前kotlin native也就苹果端支持compose
        group("apple") {
            withApple()

            group("ios") {
                withIos()
            }

            group("macos") {
                withMacos()
            }
        }
    }
}

private const val nonNative = "nonNative"
fun NamedDomainObjectContainer<KotlinSourceSet>.nonNativeMain(action: Action<KotlinSourceSet>) =
    main(nonNative, action)
private fun KotlinHierarchyBuilder.groupNonNative() {
    group(nonNative) {
        groupJsCommon()
        groupJvmCommon()
    }
}


/*
fun KotlinMultiplatformSourceSetConventions.nonJsCommon(container:NamedDomainObjectContainer<KotlinSourceSet>):KotlinSourceSet{
    return with(container){
        val nonJsCommon = create("nonJsCommon")
        nonJsCommon.dependsOn(commonMain.get())

        arrayOf(jvmMain, androidMain)
            .map { it.get() }
            .forEach {
                it.dependsOn(nonJsCommon)
            }
        nonJsCommon
    }
}*/
