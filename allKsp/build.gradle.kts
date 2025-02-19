plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.kspAnnotation)
    implementation(libs.ksp)
    implementation(libs.kotlinpoet.ksp)
}
