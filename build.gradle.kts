import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlin.composeCompiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spring) apply false
    alias(libs.plugins.srpingDependencyManagement) apply false
}

//buildscript {
//    repositories {
//        gradlePluginPortal()
//        mavenLocal()
//        google()
//        mavenCentral()
//        maven("https://maven.aliyun.com/repository/public")
//        maven("https://developer.huawei.com/repo/")
//        maven("https://jitpack.io")
//    }
//    dependencies {
//
//        classpath(libs.android.gradle)
//        classpath(libs.kotlin.gradle)
//    }
//}

subprojects {
    val isTest: String by project
    tasks.withType<KotlinCompilationTask<*>>(){
        compilerOptions {
            if (this is KotlinJvmCompilerOptions){
                if (isTest == "true")
                    freeCompilerArgs.add("-Xdebug")

                jvmTarget.set(JvmTarget.JVM_21)
                freeCompilerArgs.add("-Xjvm-default=all")
            }

            optIn.addAll(
                "kotlin.RequiresOptIn",
                "org.jetbrains.compose.resources.ExperimentalResourceApi",
                "org.koin.core.annotation.KoinExperimentalAPI"
            )

            freeCompilerArgs.addAll(
                "-Xexpect-actual-classes"
            )

        }
    }

}