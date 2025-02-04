plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spring)
    alias(libs.plugins.srpingDependencyManagement)
    alias(libs.plugins.ksp)
    application
}

version = "1.0.0"

application {
    mainClass.set("cn.allin.ApplicationKt")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}


tasks.register<Copy>("copyLibs") {
    into("build/libs/lib")
    from(configurations.runtimeClasspath)
}

tasks.bootJar {
    enabled = true
    archiveClassifier = "exec"
    setExcludes(listOf("*.jar"))
    dependsOn("copyLibs")
    manifest {
        attributes["Class-Path"] = configurations.runtimeClasspath
            .get().files
            .joinToString(" ") { "lib/${it.name}" }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    testImplementation(libs.reactor.test)
    ksp(projects.allKsp)
    ksp(libs.jimmer.ksp)
    implementation(libs.jimmer.spring)
    implementation(projects.shared)
    implementation(libs.spring.starter.webflux)
    implementation(libs.spring.starter.security)
    implementation(libs.spring.starter.cache)
    implementation(libs.spring.starter.actuator)
    implementation(libs.caffeine)
    testImplementation(libs.spring.securityTest)
    implementation(libs.spring.session.core)

    developmentOnly(libs.spring.devtools)
    runtimeOnly(libs.postgresql)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.reactor)
    testImplementation(libs.kotlin.test.junit5)

    testImplementation(libs.spring.starter.test)

}