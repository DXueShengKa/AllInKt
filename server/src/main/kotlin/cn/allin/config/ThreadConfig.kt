package cn.allin.config

import cn.allin.utils.DefaultVirtualExecutor
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class ThreadConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor? {
        return TaskExecutorAdapter(
            DefaultVirtualExecutor
        )
    }

//    @Bean
//    fun <T : ProtocolHandler> protocolHandler() = TomcatProtocolHandlerCustomizer<T> {
//        it.executor = Executors.newVirtualThreadPerTaskExecutor()
//    }
}