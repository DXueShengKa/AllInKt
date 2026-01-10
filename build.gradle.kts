plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.library.kmp) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kotlin.composeCompiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spring) apply false
    alias(libs.plugins.srpingDependencyManagement) apply false

    alias(libs.plugins.detekt.dev)
    alias(libs.plugins.ktlint)
}

detekt {
    config = files("${rootDir}config/detekt/default.yaml")
    buildUponDefaultConfig = true
}

ktlint {
    android = false

    filter {
        // 包含所有 kotlin 源并排除 build 目录
        include("**/src/**/kotlin/**")
        include("**/src/**/kotlin/**/*.kt")
        include("**/*.kts")
        exclude("**/build/**")
    }
}


dependencies {
    ktlintRuleset(libs.compose.rules.ktlint)
}
