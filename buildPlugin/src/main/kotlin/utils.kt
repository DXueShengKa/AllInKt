
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

val isMacOs = System.getProperty("os.name").startsWith("Mac OS")
val isWindow = System.getProperty("os.name").startsWith("Window")
val isLinux = System.getProperty("os.name").startsWith("Linux")

fun DependencyHandler.kspAndroid(dependencyNotation: Any) {
    add("kspAndroid", dependencyNotation)
}

fun DependencyHandler.kspJvm(dependencyNotation: Any) {
    add("kspJvm", dependencyNotation)
}

fun DependencyHandler.kspJs(dependencyNotation: Any) {
    add("kspJs", dependencyNotation)
}

fun DependencyHandler.kspIos(dependencyNotation: Any) {
    if (isMacOs) {
        add("kspIosSimulatorArm64", dependencyNotation)
        add("kspIosArm64", dependencyNotation)
        add("kspIosX64", dependencyNotation)
    }
}

fun DependencyHandler.kspAll(dependencyNotation: Any) {
    kspAndroid(dependencyNotation)
    kspJvm(dependencyNotation)
    kspIos(dependencyNotation)
    kspJs(dependencyNotation)
}

fun DependencyHandler.kspNoJsAll(dependencyNotation: Any) {
    kspAndroid(dependencyNotation)
    kspJvm(dependencyNotation)
    kspIos(dependencyNotation)
}

internal fun TaskContainer.kotlinCompilerOptions() {
    withType(KotlinCompilationTask::class) {
        compilerOptions {
            if (this is KotlinJvmCompilerOptions) {
                jvmTarget.set(JvmTarget.JVM_21)
            }

            optIn.addAll(
                "kotlin.RequiresOptIn",
                "kotlin.js.ExperimentalJsStatic",
                "kotlin.time.ExperimentalTime",
            )

            freeCompilerArgs.addAll(
                "-Xexpect-actual-classes",
            )
        }
    }
}
