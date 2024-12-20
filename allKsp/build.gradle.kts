plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.client.kspAnnotation)
    implementation(libs.ksp)
    implementation(libs.kotlinpoet.ksp)
}