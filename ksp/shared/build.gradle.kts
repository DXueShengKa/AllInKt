plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.ksp)
    implementation(libs.kotlinpoet.ksp)
    implementation("org.jetbrains.dokka:analysis-markdown:2.0.0")
    implementation("org.jetbrains.dokka:dokka-core:2.0.0")
}
