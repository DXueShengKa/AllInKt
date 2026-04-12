import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("shared-kmp")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    kotlin("kapt")
}

val isTest: String by project

kotlin {
    android {
        namespace = "cn.allin.shared"
    }
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
            implementation(npm("@js-joda/timezone", "2.3.0"))
        }
        jvmMain.dependencies {
        }
    }
}

ksp {
    arg("isTest", isTest)
}

tasks.withType<KotlinCompilationTask<*>>().all {
    val kspCommonMainKotlinMetadata = "kspCommonMainKotlinMetadata"
    if (name != kspCommonMainKotlinMetadata) {
        dependsOn(kspCommonMainKotlinMetadata)
    }
}

dependencies {
    kspCommonMainMetadata(projects.ksp.shared)

    kapt(libs.therapi.javadoc.scribe)
}
