import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "cn.allin.build.plugin"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("sharedKmp") {
            id = "allin.sharedKmp"
            implementationClass = "SharedKmpPlugin"
        }
        register("noJsKmp") {
            id = "allin.noJsKmp"
            implementationClass = "NoJsKmpPlugin"
        }
        register("composeKmp") {
            id = "allin.composeKmp"
            implementationClass = "ComposeKmpPlugin"
        }
    }
}