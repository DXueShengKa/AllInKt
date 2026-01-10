package cn.allin.config

import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.IsolationLevel
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig(
    private val connectionFactory: ConnectionFactory,
) {
    // 在应用启动时把 Spring 的 ConnectionFactory 注册到 Exposed
    @PostConstruct
    fun initExposed() {
        R2dbcDatabase.connect(
            connectionFactory,
            R2dbcDatabaseConfig {
                defaultMaxAttempts = 1
                defaultR2dbcIsolationLevel = IsolationLevel.READ_COMMITTED
                explicitDialect = PostgreSQLDialect()
            },
        )
    }
}
