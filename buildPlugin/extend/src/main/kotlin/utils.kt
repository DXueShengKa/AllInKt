import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType


val isMacOs = System.getProperty("os.name").startsWith("Mac OS")
val isMacWindow = System.getProperty("os.name").startsWith("Window")
val isMacLinux = System.getProperty("os.name").startsWith("Linux")


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


fun Project.androidConfigure() {

    val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
    configure<LibraryExtension> {
        compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()

        defaultConfig {
            minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
            consumerProguardFiles("consumer-rules.pro")
        }

        sourceSets["main"].apply {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }

        buildTypes {
            release {
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }
}

