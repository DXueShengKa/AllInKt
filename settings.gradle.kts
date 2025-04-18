
rootProject.name = "AllInKt"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("buildPlugin")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenLocal()
        mavenCentral()
    }
}

include(
//    ":composeApp",
    ":server",
    ":shared",
    ":admin",
    ":kt-ai",
    ":ksp:annotation",
    ":ksp:server",
    ":ksp:shared",
//    ":ksp:composeApp",
    ":client:net",
//    ":client:data",
//    ":client:ui",
//    ":client:components",
//    ":client:navigation-react",
)
