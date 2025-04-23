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

//把依赖的jar分开打包
tasks.register<Copy>("copyLibs") {
    into("build/libs/lib")
    from(configurations.runtimeClasspath)
}

tasks.bootJar {
    enabled = true
//    archiveClassifier = "exec"
    setExcludes(listOf("*.jar"))
    dependsOn("copyLibs")
    manifest {
        //引用分开打包的jar包
        attributes["Class-Path"] = configurations.runtimeClasspath
            .get().files
            .joinToString(" ") { "lib/${it.name}" }
    }

    //把admin打包的文件复制到静态文件里
//    from(projectDir.parent + "/admin/build/dist/js/productionExecutable") {
//        into("BOOT-INF/classes/static/admin")
//    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    ksp(projects.ksp.server)
    ksp(libs.jimmer.ksp)
    implementation(libs.jimmer.spring) {
        exclude(group = "org.babyfish.jimmer", module = "jimmer-client")
    }
    implementation(projects.shared)
    implementation(libs.spring.starter.webflux)
    implementation(libs.spring.starter.security)
    implementation(libs.spring.starter.cache)
    implementation(libs.spring.starter.actuator)
    implementation(libs.spring.starter.data.redis)
    implementation(libs.spring.session.data.redis)
    implementation(libs.spring.session.core)

    developmentOnly(libs.spring.devtools)
    implementation(libs.postgresql)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.reactor)
    compileOnly(projects.ksp.annotation)
    implementation(libs.kotlinx.serialization.protobuf)

    implementation(libs.apache.poi.ooxml)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.reactor.test)
    testImplementation(libs.spring.starter.test)
    testImplementation(libs.spring.securityTest)
}
