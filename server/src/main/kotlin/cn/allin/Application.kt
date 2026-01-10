package cn.allin

import org.babyfish.jimmer.spring.repository.EnableJimmerRepositories
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.jvm.java

@SpringBootApplication
@EnableJimmerRepositories("cn.allin.repository")
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
