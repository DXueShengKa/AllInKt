package cn.allin

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration


@SpringBootApplication(
    exclude = [ExposedAutoConfiguration::class]
)
@ImportAutoConfiguration(
    exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}