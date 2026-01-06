
rootProject.name = "AllInKt"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    includeBuild("buildPlugin")
    repositories {
        maven("https://packages.jetbrains.team/maven/p/grazi/grazie-platform-public")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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
