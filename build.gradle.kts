plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kotlin.composeCompiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spring) apply false
    alias(libs.plugins.srpingDependencyManagement) apply false

//    alias(libs.plugins.detekt.dev)
}

//detekt {
//    config.from("config/detekt/compose.yaml")
//}
//
//dependencies {
//    detektPlugins(libs.detekt.rules.ktlint)
//    detektPlugins(libs.detekt.rules.compose)
//}