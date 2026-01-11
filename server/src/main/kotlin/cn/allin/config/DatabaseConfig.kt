package cn.allin.config

import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.IsolationLevel
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig(
    private val connectionFactory: ConnectionFactory,
) {
    val databaseConfig =
        R2dbcDatabaseConfig {
            defaultMaxAttempts = 1
            defaultR2dbcIsolationLevel = IsolationLevel.READ_COMMITTED
            explicitDialect = PostgreSQLDialect()
        }

//    @Bean
//    fun r2dbcCustomConversions(): R2dbcCustomConversions {
//        val converters: List<Converter<*, *>> =
//            listOf(
//                object : Converter<UserRole, String> {
//                    override fun convert(source: UserRole): String {
//                        return source.name
//                    }
//                },
//                object : Converter<String, UserRole> {
//                    override fun convert(source: String): UserRole {
//                        return UserRole.valueOf(source)
//                    }
//                },
//            )
//        return R2dbcCustomConversions(
//            CustomConversions.StoreConversions.of(
//                SimpleTypeHolder(setOf(UserRole::class.java), true),
//                converters,
//            ),
//            converters,
//        )
//    }

    @Bean
    fun transactionManager(): ExposedTransactionManager =
        ExposedTransactionManager(
            connectionFactory,
            databaseConfig,
            showSql = true,
        )
}
