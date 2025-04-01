import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("allin.sharedKmp")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val isTest: String by project

kotlin {
    sourceSets {

        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            dependencies {
                api(libs.kotlinx.serialization.json)
                api(libs.kotlinx.datetime)
                api(libs.jetbrains.collection)
                api(libs.arrow.core)

                implementation(libs.kotlinx.serialization.protobuf)
                implementation(libs.arrow.serialization)
            }
        }

        jsMain.dependencies {
            implementation(npm("@js-joda/timezone", "2.21.1"))
        }
    }
}

ksp {
    arg("isTest", isTest)
}

android {
    namespace = "cn.allin.shared"
}

tasks.withType<KotlinCompilationTask<*>>().all {
    val kspCommonMainKotlinMetadata = "kspCommonMainKotlinMetadata"
    if (name != kspCommonMainKotlinMetadata) {
        dependsOn(kspCommonMainKotlinMetadata)
    }
}

dependencies {
    kspCommonMainMetadata(projects.allKsp)
}
