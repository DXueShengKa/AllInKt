package cn.allin

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication
@ImportAutoConfiguration(
    ExposedAutoConfiguration::class,
    exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}