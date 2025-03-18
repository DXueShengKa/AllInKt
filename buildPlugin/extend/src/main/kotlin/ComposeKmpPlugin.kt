import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * compose多平台配置，不含web
 */
class ComposeKmpPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.applyPlugin()
        project.extensions.configure<KotlinMultiplatformExtension> {
            kmp()
        }
        project.androidConfigure()
    }

    private fun Project.applyPlugin() {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")
        apply(plugin = "org.jetbrains.compose")
    }


    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.kmp() {
        applyHierarchyTemplate(hierarchyTemplate)

        androidTarget()

        jvm()

        if (isMacOs) {
            iosX64()
            iosArm64()
            iosSimulatorArm64()
        }
    }
}
