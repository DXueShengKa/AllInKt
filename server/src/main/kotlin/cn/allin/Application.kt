package cn.allin

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


//@EnableImplicitApi
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}