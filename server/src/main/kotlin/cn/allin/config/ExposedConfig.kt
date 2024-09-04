package cn.allin.config

import org.jetbrains.exposed.spring.DatabaseInitializer
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class ExposedConfig(
    private val applicationContext: ApplicationContext,
    private val dataSource: DataSource,
) {
    @Value("\${spring.exposed.excluded-packages:}#{T(java.util.Collections).emptyList()}")
    private lateinit var excludedPackages: List<String>

    @Value("\${spring.exposed.show-sql:false}")
    private var showSql: Boolean = false


    @Bean
    fun springTransactionManager(datasource: DataSource, databaseConfig: DatabaseConfig): SpringTransactionManager {
        return SpringTransactionManager(datasource, databaseConfig, showSql)
    }


    @Bean
    @ConditionalOnMissingBean(DatabaseConfig::class)
    fun databaseConfig(): DatabaseConfig {
        return DatabaseConfig {

        }
    }


    @Bean
    @ConditionalOnProperty("spring.exposed.generate-ddl", havingValue = "true", matchIfMissing = false)
    fun databaseInitializer(): DatabaseInitializer = EC(applicationContext, excludedPackages, dataSource)


    private open class EC(
        applicationContext: ApplicationContext,
        excludedPackages: List<String>,
        private val datasource: DataSource
    ) : DatabaseInitializer(applicationContext, excludedPackages) {
        override fun run(args: ApplicationArguments?) {
            val resource = ClassPathResource("db.sql")
            ScriptUtils.executeSqlScript(datasource.connection, resource)
            super.run(args)
        }
    }

}