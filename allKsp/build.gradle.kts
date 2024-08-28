plugins {
    kotlin("multiplatform")
}

kotlin {

    jvm {
        withJava()
    }

    jvmToolchain(21)

    sourceSets {
//        jvmTest.dependencies {
//            implementation(libs.junit)
//        }
        jvmMain.dependencies {
//            implementation(projects.kspAnnotation)
            implementation(libs.ksp)
            implementation(libs.kotlinpoet.ksp)
        }
    }
}
