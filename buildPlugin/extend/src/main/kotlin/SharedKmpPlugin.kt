
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * 包含所有平台的配置
 */
class SharedKmpPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.applyPlugin()

        project.extensions.configure<KotlinMultiplatformExtension> {
            kmp()
        }

        project.androidConfigure()
        project.kotlinExtension.jvmToolchain(21)
        project.tasks.kotlinCompilerOptions()
    }

    private fun Project.applyPlugin() {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
    }


    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.kmp() {
        applyHierarchyTemplate(hierarchyTemplate)

        androidTarget()

        jvm()

        js {
            browser()
            binaries.library()
        }

        if (isMacOs) {
            iosX64()
            iosArm64()
            iosSimulatorArm64()
        }
    }
}
