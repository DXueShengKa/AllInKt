plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.ksp)
    implementation(libs.kotlinpoet.ksp)
}
