
rootProject.name = "AllInKt"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }

    includeBuild("buildPlugin")
    repositories {
        maven("https://packages.jetbrains.team/maven/p/grazi/grazie-platform-public")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        maven("https://packages.jetbrains.team/maven/p/grazi/grazie-platform-public")
        google()
        mavenCentral()
    }
}

include(
    ":composeApp",
    ":server",
    ":shared",
    ":admin",
    ":kt-ai",
    ":ksp:annotation",
    ":ksp:server",
    ":ksp:shared",
    ":ksp:composeApp",
    ":client:net",
    ":client:data",
    ":client:ui",
    ":client:components",
//    ":client:navigation-react",
)
