package cn.allin.config

import io.r2dbc.spi.ConnectionFactory
import java.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.mono
import org.jetbrains.exposed.v1.core.InternalApi
import org.jetbrains.exposed.v1.core.Slf4jSqlDebugLogger
import org.jetbrains.exposed.v1.core.exposedLogger
import org.jetbrains.exposed.v1.core.transactions.ThreadLocalTransactionsStack
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction
import org.jetbrains.exposed.v1.r2dbc.transactions.transactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.reactive.AbstractReactiveTransactionManager
import org.springframework.transaction.reactive.GenericReactiveTransaction
import org.springframework.transaction.reactive.TransactionSynchronizationManager
import reactor.core.publisher.Mono
import reactor.util.retry.Retry

@OptIn(InternalApi::class)
class ExposedTransactionManager(
    connectionFactory: ConnectionFactory,
    databaseConfig: R2dbcDatabaseConfig.Builder,
    private val showSql: Boolean = false,
) : AbstractReactiveTransactionManager() {
    private val db =
        R2dbcDatabase.connect(
            connectionFactory,
            databaseConfig,
        )

    override fun doGetTransaction(synchronizationManager: TransactionSynchronizationManager): Any =
        db.transactionManager.newTransaction(
            readOnly = synchronizationManager.isCurrentTransactionReadOnly,
        )

    override fun doBegin(
        synchronizationManager: TransactionSynchronizationManager,
        transaction: Any,
        definition: TransactionDefinition,
    ): Mono<Void> =
        mono(Dispatchers.IO) {
            transaction as R2dbcTransaction
            if (showSql) {
                transaction.addLogger(Slf4jSqlDebugLogger)
            }
            ThreadLocalTransactionsStack.pushTransaction(transaction)
            synchronizationManager.bindResource(this@ExposedTransactionManager, transaction)
        }.error("doBegin")

    override fun doCommit(
        synchronizationManager: TransactionSynchronizationManager,
        status: GenericReactiveTransaction,
    ): Mono<Void> {
        val transaction = synchronizationManager.getResource(this@ExposedTransactionManager) as R2dbcTransaction
        return mono(Dispatchers.IO) {
            transaction.commit()
        }.retryWhen(
            Retry.fixedDelay(
                transaction.maxAttempts.toLong(),
                Duration.ofSeconds(transaction.maxRetryDelay / 1000),
            ),
        ).error("doCommit")
    }

    override fun doRollback(
        synchronizationManager: TransactionSynchronizationManager,
        status: GenericReactiveTransaction,
    ): Mono<Void> =
        mono(Dispatchers.IO) {
            val transaction = synchronizationManager.getResource(this@ExposedTransactionManager) as R2dbcTransaction
            transaction.rollback()
        }.error("doRollback")

    override fun doCleanupAfterCompletion(
        synchronizationManager: TransactionSynchronizationManager,
        transaction: Any,
    ): Mono<Void> =
        mono(Dispatchers.IO) {
            transaction as R2dbcTransaction
            ThreadLocalTransactionsStack.popTransaction()
            synchronizationManager.unbindResource(this@ExposedTransactionManager)
            transaction.currentStatement?.closeIfPossible()
            transaction.currentStatement = null
            transaction.close()
        }.error("doCleanupAfterCompletion")

    fun Mono<Unit>.error(tag: String): Mono<Void> =
        onErrorResume { e ->
            exposedLogger.error(tag, e)
            Mono.empty()
        }.then()
}
