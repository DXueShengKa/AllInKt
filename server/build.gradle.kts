plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spring)
    alias(libs.plugins.srpingDependencyManagement)
    application
}

application {
    mainClass.set("cn.allin.ApplicationKt")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(projects.shared)
    implementation(libs.spring.starter.web)
    implementation(libs.spring.starter.security)
    implementation(libs.spring.starter.cache)
    implementation(libs.caffeine)
    testImplementation(libs.spring.securityTest)
    implementation(libs.spring.session.core)

    implementation(libs.exposed.spring)
    implementation(libs.exposed.javaTime)
    developmentOnly(libs.spring.devtools)
    runtimeOnly(libs.postgresql)

    implementation(libs.kotlin.reflect)
    testImplementation(libs.kotlin.test.junit5)

    testImplementation(libs.spring.starter.test)

}