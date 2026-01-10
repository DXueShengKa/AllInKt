import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

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

subprojects {
    tasks.withType<KotlinCompilationTask<*>> {
        compilerOptions {
            if (this is KotlinJvmCompilerOptions) {
                jvmTarget = JvmTarget.JVM_21
            }
            if (this is KotlinJsCompilation) {
                freeCompilerArgs.add("-Xenable-suspend-function-exporting")
            }

            optIn.addAll(
                "kotlin.RequiresOptIn",
                "kotlin.js.ExperimentalJsStatic",
//                "kotlin.time.ExperimentalTime",
            )

            freeCompilerArgs.addAll(
                "-Xexpect-actual-classes",
                "-Xexplicit-backing-fields",
            )
        }
    }
}
