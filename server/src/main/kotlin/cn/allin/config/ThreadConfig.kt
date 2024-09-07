package cn.allin.config

import org.apache.coyote.ProtocolHandler
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableAsync
class ThreadConfig : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor? {
        return TaskExecutorAdapter(
            Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("async-virtual#").factory()
            )
        )
    }

    @Bean
    fun <T : ProtocolHandler> protocolHandler() = TomcatProtocolHandlerCustomizer<T> {
        it.executor = Executors.newVirtualThreadPerTaskExecutor()
    }
}